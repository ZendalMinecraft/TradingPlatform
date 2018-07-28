/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.scalecube.socketio.Session;
import io.scalecube.socketio.SocketIOListener;
import io.scalecube.socketio.SocketIOServer;
import org.bson.Document;
import ru.zendal.config.bundle.SocketConfigBundle;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.socket.exception.SocketIOException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SocketIO implements SocketServer {

    private SocketIOServer server;
    private List<Session> storageSessions = new ArrayList<>();

    private final TradeSessionManager platform;
    private Charset charset;
    private final Logger logger;

    public SocketIO(SocketConfigBundle socketConfigBundle,
                    TradeSessionManager tradingPlatform,
                    Logger logger) {
        this.initServer(socketConfigBundle);
        this.prepareServer();
        platform = tradingPlatform;
        this.logger = logger;
    }

    /**
     * Init server
     *
     * @param socketConfigBundle Configuration data
     */
    private void initServer(SocketConfigBundle socketConfigBundle) {
        charset = Charset.forName(socketConfigBundle.getCharset());
        server = SocketIOServer.newInstance(socketConfigBundle.getPort());
    }

    /**
     * Prepare Server
     */
    private void prepareServer() {
        server.setListener(new SocketIOListener() {
            @Override
            public void onConnect(Session session) {
                storageSessions.add(session);
                logger.fine("New connection: "
                        + session.getRemoteAddress().toString()
                        + ":" + session.getLocalPort()
                );
            }

            @Override
            public void onMessage(Session session, ByteBuf message) {
                try {
                    session.send(convertStringToByteBuff(processMessage(session, message), charset));
                } catch (SocketIOException e) {
                    session.send(convertStringToByteBuff(e.getMessage(), charset));
                }
            }

            @Override
            public void onDisconnect(Session session) {
                storageSessions.remove(session);
                logger.fine("Close  connection: "
                        + session.getRemoteAddress().toString()
                        + ":" + session.getLocalPort()
                );
            }
        });
    }

    /**
     * Process message from client
     *
     * @param session Session
     * @param message Byte buf message
     */
    private String processMessage(Session session, ByteBuf message) throws SocketIOException {
        String text = message.toString(charset);
        Document jsonDocument = Document.parse(text);
        String command = jsonDocument.getString("command");
        if (command == null) {
            throw new SocketIOException("Missing required key 'command'");
        }
        if (command.equalsIgnoreCase("auth")) {

        } else if (command.equalsIgnoreCase("getAllOfflineTrades")) {
            return this.getAllOfflineTrades();
        }
        return "";
    }

    private String getAllOfflineTrades() {
        Document response = new Document();
        List<Document> documents = new ArrayList<>();
        platform.getAllTradeOffline().forEach(tradeOffline -> {
            Document collection = new Document();
            collection.put("name", tradeOffline.getOfflinePlayer().getName());
            documents.add(collection);
        });
        response.put("data", documents);
        return response.toJson();
    }

    private void processCommand() {

    }

    @Override
    public boolean start() {
        try {
            server.start();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean stop() {
        try {
            server.stop();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Convert String to ByteBuff
     *
     * @param data    String data
     * @param charset Charset data
     * @return ByteBuf
     */
    private ByteBuf convertStringToByteBuff(String data, Charset charset) {
        return Unpooled.copiedBuffer(data.toCharArray(), charset);
    }
}
