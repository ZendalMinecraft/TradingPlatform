/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config.bundle;

import java.util.List;

public class InventoryConfigBundle {

    private List<Double> betSpread;


    public InventoryConfigBundle(List<Double> betSpread) {
        this.betSpread = betSpread;
    }

    public List<Double> getBetSpread() {
        return this.betSpread;
    }
}
