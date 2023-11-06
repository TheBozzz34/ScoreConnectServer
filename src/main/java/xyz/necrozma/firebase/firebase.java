package xyz.necrozma.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class firebase {

    protected static final Logger logger = LogManager.getLogger(firebase.class);

    public static void init() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(firebase.class.getResourceAsStream("/scoreconnect-a8d83-firebase-adminsdk-1k3hp-4403fcc6c2.json"));
        googleCredentials.refresh();


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(googleCredentials)
                .build();

        try {
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            logger.error("Firebase unable to initialize: " + e.getMessage());
        }



    }
}
