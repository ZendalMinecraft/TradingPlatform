/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config.bundle;

import ru.zendal.config.bundle.builder.InventoryConfigBundleBuilder;

import java.util.List;

public class InventoryConfigBundle {

    private List<Double> betSpread;

    private boolean economyEnable = false;

    public InventoryConfigBundle(InventoryConfigBundleBuilder bundleBuilder) {
        this.prepareBuilder(bundleBuilder);
    }

    private void prepareBuilder(InventoryConfigBundleBuilder bundleBuilder) {
        if (bundleBuilder.hasBetSpread()) {
            betSpread = bundleBuilder.getBetSpread();
        }

        if (bundleBuilder.hasFlagEconomyEnable()) {
            economyEnable = bundleBuilder.getFlagEconomyEnable();
        }
    }

    public List<Double> getBetSpread() {
        return this.betSpread;
    }

    public boolean isEconomyEnable() {
        return economyEnable;
    }
}
