/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.socket;

import io.scalecube.socketio.SocketIOServer;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import ru.zendal.config.bundle.SocketConfigBundle;
import ru.zendal.entity.ExtendedItemStack;
import ru.zendal.service.economy.EconomyProvider;
import ru.zendal.session.TradeOffline;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.socket.command.AcceptTradeCommand;
import ru.zendal.socket.command.GetAllOfflineTradesCommand;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation Socket Server with the help io.scalecube.socketio
 *
 * @see SocketIOServer
 */
public class SocketIO implements SocketServer {

    /**
     * Session manager
     */
    private final TradeSessionManager sessionManager;
    private final EconomyProvider economyProvider;
    /**
     * Instance socket Server
     */
    private SocketIOServer server;


    /**
     * Instance logger
     */
    private final Logger logger;

    /**
     * Adapter Listener
     */
    private AdapterServerListener adapterServerListener;

    /**
     * Constructor Server
     *
     * @param socketConfigBundle Config bundle for setup server
     * @param sessionManager     Instance session Manager
     * @param economyProvider    Instance Economy Provider
     * @param logger             instance logger
     */
    public SocketIO(SocketConfigBundle socketConfigBundle,
                    TradeSessionManager sessionManager,
                    EconomyProvider economyProvider,
                    Logger logger) {
        this.sessionManager = sessionManager;
        this.economyProvider = economyProvider;
        this.logger = logger;
        this.initServer(socketConfigBundle);
        this.prepareServer();
    }


    /**
     * Init server
     *
     * @param socketConfigBundle Configuration data
     */
    private void initServer(SocketConfigBundle socketConfigBundle) {
        Charset messageCharset = Charset.forName(socketConfigBundle.getCharset());
        server = SocketIOServer.newInstance(socketConfigBundle.getPort());

        adapterServerListener = new AdapterServerListener(messageCharset, logger);
        adapterServerListener.addCommandProcessors(new GetAllOfflineTradesCommand(sessionManager));
        adapterServerListener.addCommandProcessors(new AcceptTradeCommand(sessionManager, economyProvider));
        sessionManager.addListenerOnCreateNewOfflineTrade(this::processCreateNewOfflineTrade);
    }

    /**
     * Prepare Server
     */
    private void prepareServer() {
        server.setListener(adapterServerListener);
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
     * Processor event Create new offline trade
     *
     * @param tradeOffline Offline trade
     */
    private void processCreateNewOfflineTrade(TradeOffline tradeOffline) {
        Document document = this.getResponseMessageAboutCreateNewOfflineTrade(tradeOffline);
        adapterServerListener.sendMessageAllSessions(document);
    }

    /**
     * Get response message about create new offline trade
     *
     * @param tradeOffline Offline trade
     * @return BSON Document
     */
    private Document getResponseMessageAboutCreateNewOfflineTrade(TradeOffline tradeOffline) {
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
        return response;
    }


    /**
     * Get list documents BSON by List ItemStack
     *
     * @param itemStackList List ItemStack
     * @return List Document BSON
     */
    private List<Document> getListDocumentByListItemStack(List<ItemStack> itemStackList) {
        List<Document> documentList = new ArrayList<>();

        for (ItemStack itemStack : itemStackList) {
            documentList.add(new ExtendedItemStack(itemStack).toDocument());
        }
        return documentList;
    }
}
