package xyz.necrozma.message;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;


import java.util.Arrays;

import static xyz.necrozma.firebase.firebase.verifyMessageToken;

public class MessageHandler {

    protected static final Logger logger = LogManager.getLogger(MessageHandler.class);

    public static void HandleMessage(Message message, WebSocket conn) {
        String[] knownTypeIntegers = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

        // 1 = ping
        // 2 = Get device status
        // 3 = Get device info
        // 4 = Get device list
        // 5 = Send device command
        // 6 = Send device data
        // 7 = Send device data
        // 8 = Send device data
        // 9 = TBD



        if (message == null) {
            logger.error("Message is null.");
            return;
        }

        if (Arrays.asList(knownTypeIntegers).contains(String.valueOf(message.getType()))) {
            logger.info("Message type is known.");

            boolean validUser = verifyMessageToken(message.getToken());

            if (!validUser) {
                logger.warn("User is not valid.");
                Message response = new Message();

                response.setId(1);
                response.setType(1);
                response.setText("invalid token");

                logger.info("Sending response: " + new Gson().toJson(response));

                conn.send(new Gson().toJson(response));
                return;
            } else {
                logger.info("User is valid.");
            }


            switch(message.getType()) {
                case 1:
                    logger.info("Message type is 1 (ping).");
                    Message response = new Message();

                    response.setId(1);
                    response.setType(0);
                    response.setText("pong");

                    logger.info("Sending response: " + new Gson().toJson(response));

                    conn.send(new Gson().toJson(response));
                    break;

                default:
                    logger.warn("Message type not handled!");
                    Message response2 = new Message();

                    response2.setId(1);
                    response2.setType(1);
                    response2.setText("invalid message type");

                    logger.info("Sending response: " + new Gson().toJson(response2));

                    conn.send(new Gson().toJson(response2));
                    break;
            }

        } else {
            logger.warn("Message type is unknown.");
            Message response = new Message();

            response.setId(1);
            response.setType(1);
            response.setText("invalid message type");

            logger.info("Sending response: " + new Gson().toJson(response));

            conn.send(new Gson().toJson(response));
        }
    }

}
