package xyz.necrozma.message;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;


import java.util.ArrayList;
import java.util.Arrays;

import static xyz.necrozma.device.HardwareConnection.getDeviceList;
import static xyz.necrozma.device.HardwareConnection.getDeviceStatus;
import static xyz.necrozma.firebase.firebase.verifyMessageToken;

public class MessageHandler {

    protected static final Logger logger = LogManager.getLogger(MessageHandler.class);

    public static void sendMessage(int id, int type, String text, WebSocket conn) {
        Message message = new Message();

        message.setId(id);
        message.setType(type);
        message.setText(text);

        conn.send(new Gson().toJson(message));
    }

    public static void HandleMessage(Message message, WebSocket conn) {
        String[] knownTypeIntegers = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

        String sampleDeviceInfoJson = """
                {
                  "deviceID": "deviceID",
                  "deviceName": "deviceName",
                  "deviceType": "deviceType",
                  "deviceStatus": "deviceStatus",
                  "deviceData": "deviceData",
                  "deviceCommands": "deviceCommands",
                  "teamAName": "teamAName",
                  "teamBName": "teamBName",
                  "teamAScore": "6",
                  "teamBScore": "3"
                }
                """;

        // 1 = ping
        // 2 = Get device status/info
        // 3 = TBD
        // 4 = Get device list
        // 5 = Send device command
        // 6 = Send device data
        // 7 = Send device data
        // 8 = Get device data
        // 9 = TBD



        if (message == null) {
            logger.error("Message is null.");
            return;
        }

        if (Arrays.asList(knownTypeIntegers).contains(String.valueOf(message.getType()))) {
            logger.info("Message type is known.");

            // boolean validUser = verifyMessageToken(message.getToken());
            boolean validUser = true; // Testing

            if (!validUser) {
                logger.warn("User is not valid.");
                sendMessage(1, 1, "invalid token", conn);
                return;
            } else {
                logger.info("User is valid.");
            }

            switch (message.getType()) {
                case 1 -> {
                    logger.info("Message type is 1 (ping).");
                    sendMessage(1, 1, "pong", conn);
                }
                case 2 -> {
                    logger.info("Message type is 2 (get device status/info).");
                    String deviceStatus = getDeviceStatus(message.getText());
                    sendMessage(1, 2, deviceStatus, conn);
                }
                case 4 -> {
                    logger.info("Message type is 4 (get device list).");
                    ArrayList<String> deviceList = getDeviceList();
                    sendMessage(1, 4, new Gson().toJson(deviceList), conn);
                }
                case 5 -> {
                    logger.info("Message type is 5 (send device command).");
                    sendMessage(1, 5, "device command sent", conn);
                }
                case 6, 7 -> {
                    logger.info("Message type is 5 (send device data).");
                    sendMessage(1, 5, "device data sent", conn);
                }
                case 8 -> {
                    logger.info("Message type is 8 (get device data).");
                    logger.info("Sending sample device info: " + sampleDeviceInfoJson);
                    sendMessage(1, 18, sampleDeviceInfoJson, conn);
                }
                default -> {
                    logger.warn("Message type not handled!");
                    sendMessage(1, 1, "invalid message type", conn);
                }
            }

        } else {
            logger.warn("Message type is unknown.");
            sendMessage(1, 1, "invalid message type", conn);
        }
    }

}
