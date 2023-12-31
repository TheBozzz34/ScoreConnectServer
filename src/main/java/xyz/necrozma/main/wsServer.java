/*
    Copyright (C) 2023  Ethan James

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

 */



package xyz.necrozma.main;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import xyz.necrozma.message.Message;
import xyz.necrozma.message.MessageHandler;
import xyz.necrozma.message.MessageParser;

import static xyz.necrozma.firebase.firebase.initFirebase;

public class wsServer extends WebSocketServer {
    protected static final Logger logger = LogManager.getLogger(wsServer.class);

    Gson gson = new Gson();
    public wsServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String connectionResponse = """
                {
                  "id": 1,
                  "type": 1,
                  "text": "connection successful"
                }""";
        conn.send(connectionResponse);


        // broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
        logger.info("new connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        logger.info("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        logger.info("received message from "	+ conn.getRemoteSocketAddress());

        Message messageObject = MessageParser.parse(message);

        if (messageObject == null) {
            logger.warn("Failed to parse the received message.");

            Message response = new Message();

            response.setId(1);
            response.setType(1);
            response.setText("invalid message type");

            logger.info("Sending response: " + new Gson().toJson(response));

            conn.send(new Gson().toJson(response));

            return;
        }

        MessageHandler.HandleMessage(messageObject, conn);

    }

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
        logger.info("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        logger.error("an error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }

    @Override
    public void onStart() {
        logger.info("server started successfully");
    }


    public static void main(String[] args) throws InterruptedException, IOException {

        initFirebase();

        String host = "localhost";
        int port = 8887;

        WebSocketServer server = new wsServer(new InetSocketAddress(host, port));

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    Thread.sleep(200);
                    logger.info("Shutting down ...");

                    server.stop();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("Error shutting down: " + e.getMessage());
                }
            }
        });

        server.run();

    }
}