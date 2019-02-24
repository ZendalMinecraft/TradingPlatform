/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.zendal.core.core.session.modal;

import java.util.Collection;

/**
 * Interface of session.
 *
 * @param <T> type of Trader (Player)
 * @param <I> type of item (ItemStack)
 */
public interface Session<T, I> {
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
}
