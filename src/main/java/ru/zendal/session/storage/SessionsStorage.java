/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.storage;

import ru.zendal.session.Session;
import ru.zendal.session.TradeOffline;
import ru.zendal.session.TradeOfflineSession;

import java.util.List;

public interface SessionsStorage {


    /**
     * Get last connection status
     *
     * @return {@code true} if connection exists else {@code false}
     */
    boolean isAvailable();


    /**
     * Save  session into Storage
     *
     * @param session Offline session
     * @return unique Id session
     */
    String saveSession(TradeOfflineSession session);

    /**
     * Get all Sessions
     *
     * @return List Sessions
     */
    List<TradeOffline> getAllSessions();

    /**
     * Remove trade offer from storage
     *
     * @param tradeOffline Trade offline
     */
    void removeTradeOffline(TradeOffline tradeOffline);
}
