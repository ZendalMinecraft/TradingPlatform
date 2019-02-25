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
public class DefaultTradeSession<T, I> implements TradeSession<T, I> {

    /**
     * Instance of trader who create trade session
     */
    private final T mainTrader;

    /**
     * Items main trader
     */
    private Collection<I> itemsMainTraider;

    /**
     * Bet of main trader
     */
    private Double betMainTrader = 0.0;

    /**
     * Ready status of main trader
     */
    private Boolean mainTraderIsReady = false;

    /**
     * Instance of trader who accept trade session
     */
    private final T secondaryTrader;

    /**
     * Items secondary trader
     */
    private Collection<I> itemsSecondaryTrader;

    /**
     * Bet of secondary trader
     */
    private double betSecondaryTrader = 0.0;

    /**
     * Ready status of secondary trader
     */
    private Boolean secondaryTraderIsReady = false;


    /**
     * Instantiates a new Default trade session.
     *
     * @param mainTrader      the main trader
     * @param secondaryTrader the secondary trader
     */
    public DefaultTradeSession(T mainTrader, T secondaryTrader) {
        this.mainTrader = mainTrader;
        this.secondaryTrader = secondaryTrader;
    }

    @Override
    public T getMainTrader() {
        return mainTrader;
    }

    @Override
    public Collection<I> getItemsMainTrader() {
        return itemsMainTraider;
    }

    @Override
    public Double getBetMainTrader() {
        return betMainTrader;
    }

    @Override
    public void setBetMainTrader(Double betMainTrader) {
        this.betMainTrader = betMainTrader;
    }

    /**
     * Set status ready for main trader
     *
     * @param status {@code true} if ready else {@code false}
     */
    public void setStatusReadyMainTrader(Boolean status) {
        this.mainTraderIsReady = status;
    }

    public T getSecondaryTrader() {
        return secondaryTrader;
    }


    public Collection<I> getItemsSecondaryTrader() {
        return itemsSecondaryTrader;
    }

    public Double getBetSecondaryTrader() {
        return betSecondaryTrader;
    }

    public void setBetSecondaryTrader(Double betSecondaryTrader) {
        this.betSecondaryTrader = betSecondaryTrader;
    }

    public void setStatusReadySecondaryTrader(Boolean status) {
        this.secondaryTraderIsReady = status;
    }

    public boolean tradeIsReady() {
        return this.mainTraderIsReady && this.secondaryTraderIsReady;
    }
}
