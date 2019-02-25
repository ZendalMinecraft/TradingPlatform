/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.core.session.modal;

import java.util.Collection;

/**
 * Interface of session.
 *
 * @param <T> type of Trader (Player)
 * @param <I> type of item (ItemStack)
 */
public interface TradeSession<T, I> {
    /**
     * Get instance of main trader
     *
     * @return Instance of trader who create trade session
     */
    T getMainTrader();

    /**
     * Get items main trader
     *
     * @return collection of items
     */
    Collection<I> getItemsMainTrader();

    /**
     * Get bet of main trader
     *
     * @return amount bet main trader
     */
    Double getBetMainTrader();

    /**
     * Set bet for main trader
     *
     * @param betMainTrader amount bet main trader
     */
    void setBetMainTrader(Double betMainTrader);
}
