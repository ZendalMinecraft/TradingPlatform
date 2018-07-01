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

public interface StorageSessions {


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
    String saveSession(Session session);

    /**
     * Get all Sessions
     *
     * @return List Sessions
     */
    List<TradeOffline> getAllSessions();

    void removeTradeOffline(TradeOffline tradeOffline);
}
