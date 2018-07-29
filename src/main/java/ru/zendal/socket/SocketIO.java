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
import org.bukkit.inventory.ItemStack;
import ru.zendal.config.bundle.SocketConfigBundle;
import ru.zendal.entity.ExtendedItemStack;
import ru.zendal.session.TradeOffline;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.socket.command.Command;
import ru.zendal.socket.command.GetAllOfflineTradesCommand;
import ru.zendal.socket.exception.CommandProcessSocketIOException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SocketIO implements SocketServer {

    private final TradeSessionManager sessionManager;
    private SocketIOServer server;
    private List<Session> storageSessions = new ArrayList<>();

    private Charset charset;
    private final Logger logger;

    private List<Command> serverCommandList = new ArrayList<>();

    public SocketIO(SocketConfigBundle socketConfigBundle,
                    TradeSessionManager sessionManager,
                    Logger logger) {
        this.sessionManager = sessionManager;
        this.logger = logger;
        this.initServer(socketConfigBundle);
        this.prepareServer();
        sessionManager.addListenerOnCreateNewOfflineTrade(this::processNew);
    }

    private void processNew(TradeOffline tradeOffline) {
        Document response = new Document();
        Document data = new Document();
        List<Document> documents = new ArrayList<>();
        Document offlineTradeDocument = new Document();

        //Put data about trade
        offlineTradeDocument.put("id", tradeOffline.getUniqueId());

        //Put Trader
        Document traderCollection = new Document();
        traderCollection.put("name", tradeOffline.getOfflinePlayer().getName());
        traderCollection.put("uuid", tradeOffline.getOfflinePlayer().getUniqueId().toString());
        offlineTradeDocument.put("trader", traderCollection);


        //Put items
        offlineTradeDocument.put("hasItems", this.getListDocumentByListItemStack(tradeOffline.getHas()));
        offlineTradeDocument.put("wantItems", this.getListDocumentByListItemStack(tradeOffline.getWants()));
        documents.add(offlineTradeDocument);

        data.put("type", "newOfflineTrade");
        data.put("trade", documents);
        response.put("code", 0);
        response.put("response", data);

        for (Session session : storageSessions) {
            this.sendMessage(session, response);
        }
    }


    private List<Document> getListDocumentByListItemStack(List<ItemStack> has) {
        List<Document> documentList = new ArrayList<>();

        for (ItemStack itemStack : has) {
            documentList.add(new ExtendedItemStack(itemStack).toDocument());
        }
        return documentList;
    }

    /**
     * Init server
     *
     * @param socketConfigBundle Configuration data
     */
    private void initServer(SocketConfigBundle socketConfigBundle) {
        charset = Charset.forName(socketConfigBundle.getCharset());
        server = SocketIOServer.newInstance(socketConfigBundle.getPort());
        serverCommandList.add(new GetAllOfflineTradesCommand(this.sessionManager));
    }

    /**
     * Prepare Server
     */
    private void prepareServer() {
        server.setListener(new SocketIOListener() {
            @Override
            public void onConnect(Session session) {
                storageSessions.add(session);
                logger.info("New connection: "
                        + session.getRemoteAddress().toString()
                );
            }

            @Override
            public void onMessage(Session session, ByteBuf message) {
                Document response = new Document();
                try {
                    Document data = processMessage(session, message);
                    response.put("code", 0);
                    response.put("response", data);
                    sendMessage(session, response);
                } catch (CommandProcessSocketIOException e) {
                    response.put("code", e.getErrorCode());
                    response.put("errorMessage", e.getMessage());
                    sendMessage(session, response);
                }
            }

            @Override
            public void onDisconnect(Session session) {
                storageSessions.remove(session);
                logger.info("Close  connection: "
                        + session.getRemoteAddress().toString()
                );
            }
        });
    }


    private void sendMessage(Session session, Document message) {
        session.send(convertStringToByteBuff(message.toJson(), charset));
    }

    /**
     * Process message from client
     *
     * @param session Session
     * @param message Byte buf message
     */
    private Document processMessage(Session session, ByteBuf message) throws CommandProcessSocketIOException {

        String text = message.toString(charset);
        Document jsonDocument = Document.parse(text);
        String commandName = jsonDocument.getString("command");
        if (commandName == null) {
            throw new CommandProcessSocketIOException(
                    "Missing required key 'command'",
                    CommandProcessSocketIOException.CODE_MISSING_REQUIRED_KEY
            );
        }

        for (Command command : serverCommandList) {
            if (command.canProcess(jsonDocument)) {
                return command.process(jsonDocument);
            }
        }

        throw new CommandProcessSocketIOException(
                "Undefined command",
                CommandProcessSocketIOException.CODE_UNDEFINED_COMMAND
        );
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
