/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.storage;

import org.dizitart.no2.NitriteCollection;
import ru.zendal.session.Session;
import ru.zendal.session.TradeOffline;
import ru.zendal.session.storage.connection.builder.NitriteConnectionBuilder;

import java.util.List;

public class NitriteStorageSessions implements StorageSessions{


    private final NitriteCollection builder;

    public NitriteStorageSessions(NitriteConnectionBuilder builder){
        this.builder = builder.build().getCollection("TradingPlatform");
    }


    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public String saveSession(Session session) {
        return null;
    }

    @Override
    public List<TradeOffline> getAllSessions() {
        return null;
    }
}
