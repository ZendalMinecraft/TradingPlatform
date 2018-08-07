/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.storage;

import ru.zendal.session.Session;
import ru.zendal.session.TradeOffline;

import java.util.List;

public class PacifierStorage implements SessionsStorage {
    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public String saveSession(Session session) {
        return "NOT_VALID_ID_SESSION";
    }

    @Override
    public List<TradeOffline> getAllSessions() {
        return null;
    }

    @Override
    public void removeTradeOffline(TradeOffline tradeOffline) {

    }
}
