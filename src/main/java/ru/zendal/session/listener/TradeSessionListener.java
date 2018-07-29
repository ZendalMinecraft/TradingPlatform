/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.listener;

import ru.zendal.session.TradeOffline;
import ru.zendal.session.TradeOfflineSession;

public interface TradeSessionListener {

    void onCreateNewOfflineTradeSession(TradeOffline tradeOffline);
}
