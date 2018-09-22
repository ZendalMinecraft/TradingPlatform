/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config.bundle.builder;

import ru.zendal.config.bundle.InventoryConfigBundle;
import ru.zendal.config.bundle.builder.exception.FieldNotInitializedException;

import java.util.List;

/**
 * InventoryConfigBundle Builder
 */
public final class InventoryConfigBundleBuilder {
    /**
     * Bet spread
     */
    private List<Double> betSpread;

    /**
     * Economy is enable
     */
    private Boolean economyEnable;

    /**
     * Setup bet spread for pick amount bet into trades
     *
     * @param betSpread List bet
     * @return this
     */
    public InventoryConfigBundleBuilder setBetSpread(List<Double> betSpread) {
        this.betSpread = betSpread;
        return this;
    }

    /**
     * Set flag for detect Enable Economy
     *
     * @param flagEnableEconomy is enable
     * @return this
     */
    public InventoryConfigBundleBuilder setFlagEnableEconomy(boolean flagEnableEconomy) {
        this.economyEnable = flagEnableEconomy;
        return this;
    }

    /**
     * Get bet spread
     *
     * @return List bet spread
     */
    public List<Double> getBetSpread() throws FieldNotInitializedException {
        if (null == betSpread) {
            throw new FieldNotInitializedException("Bet spread not initialized");
        }
        return betSpread;
    }

    /**
     * Get flag enable economy
     *
     * @return flag
     */
    public boolean getFlagEconomyEnable() throws FieldNotInitializedException {
        if (null == economyEnable) {
            throw new FieldNotInitializedException("Flag economy enable not initialized");
        }
        return economyEnable;
    }

    /**
     * Bet is spread
     *
     * @return {@code true} if initialized else {@code false}
     */
    public boolean hasBetSpread() {
        return null != betSpread;
    }

    /**
     * Field is enable
     *
     * @return {@code true} if initialized else {@code false}
     */
    public boolean hasFlagEconomyEnable() {
        return null != economyEnable;
    }

    /**
     * Build InventoryConfigBundle
     *
     * @return InventoryConfigBundle
     */
    public InventoryConfigBundle build() {
        return new InventoryConfigBundle(this);
    }


}
