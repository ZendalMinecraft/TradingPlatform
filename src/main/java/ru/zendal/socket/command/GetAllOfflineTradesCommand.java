/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.socket.command;

import org.bson.Document;
import ru.zendal.session.TradeSessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Command "get all Offline trades"
 */
public class GetAllOfflineTradesCommand implements Command {


    private final TradeSessionManager sessionManager;

    public GetAllOfflineTradesCommand(TradeSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Document process(Document incomingDocument) {
        Document response = new Document();
        List<Document> documents = new ArrayList<>();
        sessionManager.getAllTradeOffline().forEach(tradeOffline -> {
            Document offlineTradeDocument = new Document();

            //Put data about trade
            offlineTradeDocument.put("id",tradeOffline.getUniqueId());

            //Put Trader
            Document traderCollection = new Document();
            traderCollection.put("name",tradeOffline.getOfflinePlayer().getName());
            traderCollection.put("uuid",tradeOffline.getOfflinePlayer().getUniqueId().toString());
            offlineTradeDocument.put("trader",traderCollection);



            //Put items
            offlineTradeDocument.put("hasItems",tradeOffline.getHas());
            documents.add(offlineTradeDocument);
        });
        response.put("data", documents);
        return response;
    }

    @Override
    public boolean canProcess(Document incomingDocument) {

        return false;
    }
}
