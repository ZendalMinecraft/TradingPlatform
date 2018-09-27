/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.storage;

import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import ru.zendal.session.TradeOffline;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.storage.connection.builder.NitriteConnectionBuilder;

import java.util.List;

public class NitriteStorageSessions implements SessionsStorage {


    private final NitriteCollection collection;

    public NitriteStorageSessions(NitriteConnectionBuilder collection) {
        this.collection = collection.build().getCollection("TradingPlatform");
    }


    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String saveSession(TradeOfflineSession session) {
        Document document = new Document();







        collection.insert(document);

        return null;
    }

    @Override
    public List<TradeOffline> getAllSessions() {
        return null;
    }

    @Override
    public void removeTradeOffline(TradeOffline tradeOffline) {

    }
}
