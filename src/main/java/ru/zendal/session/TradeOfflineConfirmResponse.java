/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TradeOfflineConfirmResponse {
    private final List<ItemStack> listMissingItems;
    private final List<ItemStack> newHasItems;

    public TradeOfflineConfirmResponse(List<ItemStack> listMissingItems, List<ItemStack> newHasItems) {
        this.listMissingItems = listMissingItems;
        this.newHasItems = newHasItems;
    }


    public boolean hasMissingItems() {
        return listMissingItems.size() != 0;
    }

    public List<ItemStack> getListMissingItems() {
        return listMissingItems;
    }

    public ItemStack[] getNewContent() {
        return newHasItems.toArray(new ItemStack[0]);
    }
}
