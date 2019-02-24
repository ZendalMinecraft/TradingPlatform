/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.core.session.modal;

import java.util.Collection;

/**
 * Implement of session model.
 * Model for default type trade
 *
 * @param <T> Type of trader.
 * @param <I> type of item (ItemStack).
 */
public class DefaultTradeSession<T, I> implements Session<T, I> {

    /**
     * Instance of trader who create trade session
     */
    private final T mainTrader;

    /**
     * Instance of trader who accept trade session
     */
    private final T secondaryTrader;


    private Collection<I> itemsMainTraider;


    public DefaultTradeSession(T mainTrader, T secondaryTrader) {
        this.mainTrader = mainTrader;
        this.secondaryTrader = secondaryTrader;
    }

    @Override
    public T getMainTrader() {
        return mainTrader;
    }

    public T getSecondaryTrader() {
        return secondaryTrader;
    }


    @Override
    public Collection<I> getItemsMainTrader() {
        return itemsMainTraider;
    }
}
