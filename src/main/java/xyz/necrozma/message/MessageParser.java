package xyz.necrozma.message;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageParser {
    protected static final Logger logger = LogManager.getLogger(MessageParser.class);
    private static final Gson gson = new Gson();
    public static Message parse(String rawMessage) {
        try {
            logger.info("Parsing message");
            return gson.fromJson(rawMessage, Message.class);
        } catch (Exception e) {
            logger.error("Error parsing the message: " + e.getMessage());
            return null;
        }
    }
}
