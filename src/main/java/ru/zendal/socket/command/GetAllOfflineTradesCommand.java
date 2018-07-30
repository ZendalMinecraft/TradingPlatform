/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.socket.command;

import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import ru.zendal.entity.ExtendedItemStack;
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
    public Document process(Document incomingDocument)  {
        Document response = new Document();
        List<Document> documents = new ArrayList<>();
        sessionManager.getAllTradeOffline().forEach(tradeOffline -> {
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
        });
        response.put("type", "getAllOfflineTrades");
        response.put("trades", documents);
        return response;
    }


    private List<Document> getListDocumentByListItemStack(List<ItemStack> has) {
        List<Document> documentList = new ArrayList<>();

        for (ItemStack itemStack : has) {
            documentList.add(new ExtendedItemStack(itemStack).toDocument());
        }
        return documentList;
    }

    @Override
    public boolean canProcess(Document incomingDocument) {
        String nameCommand = incomingDocument.getString("command");
        if (nameCommand != null) {
            return nameCommand.equalsIgnoreCase("getAllOfflineTrades");
        }
        return false;
    }
}
