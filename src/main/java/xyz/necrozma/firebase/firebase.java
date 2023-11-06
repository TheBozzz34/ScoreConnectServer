package xyz.necrozma.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

public class firebase {

    protected static final Logger logger = LogManager.getLogger(firebase.class);

    public static void initFirebase() throws IOException {

        InputStream serviceAccount = firebase.class.getResourceAsStream("/scoreconnect-a8d83-firebase-adminsdk-1k3hp-4403fcc6c2.json");


        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(serviceAccount);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(googleCredentials)
                .build();

        try {
            FirebaseApp.initializeApp(options);
            logger.info("Firebase initialized.");
        } catch (Exception e) {
            logger.error("Firebase unable to initialize: " + e.getMessage());
        }

    }

    public static String decodeTokenToUID(String token) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            return decodedToken.getUid();
        } catch (Exception e) {
            logger.error("Error decoding token: " + e.getMessage());
            return null;
        }
    }


    public static boolean checkIfUserExists(String uid) {
        try {
            FirebaseAuth.getInstance().getUser(uid);
            return true;
        } catch (Exception e) {
            logger.error("Error checking if user exists: " + e.getMessage());
            return false;
        }
    }


    public static boolean verifyMessageToken(String token) {
        String uid = decodeTokenToUID(token);
        if (uid == null) {
            return false;
        }
        return checkIfUserExists(uid);
    }
}
