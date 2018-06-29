/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session;

/**
 * Callback for Sessions trade
 */
public interface TradeSessionCallback {

    /**
     * Called when both participants are ready to trade
     *
     * @param tradeSession instance session Trade
     */
    public void onReady(Session tradeSession);

    /**
     * Called when there is only process the exchange
     *
     * @param tradeSession instance session Trade
     */
    public void processTrade(Session tradeSession);
}
