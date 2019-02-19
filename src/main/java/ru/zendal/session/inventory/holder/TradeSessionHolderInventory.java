/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.inventory.holder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import ru.zendal.util.ItemBuilder;

public class TradeSessionHolderInventory implements InventoryHolder {

    private Inventory inventory;

    @Override
    public Inventory getInventory() {
        if (this.inventory == null) {
            this.initializeInventory();
        }
        return this.inventory;
    }

    private void initializeInventory() {
        inventory = Bukkit.createInventory(this, 9 * 6);
        ItemStack stick = new ItemStack(Material.STICK);
        for (int i = 0; i < 6; i++) {
            inventory.setItem(9 * i + 4, stick);
        }

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);

        this.insertItemBetweenIndex(inventory, glass, 45, 53);

        ItemStack redWool = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemStack greenWool = new ItemStack(Material.WOOL, 1, (short) 5);

        inventory.setItem(9 * 4 + 4, redWool);
        inventory.setItem(9 + 4, greenWool);
        inventory.setItem(9 * 5 + 4, stick);
    }

    /**
     *
     * @param visualItem
     */
    public void setVisualDisplayBet(ItemStack visualItem){
        inventory.setItem(9 * 5 + 4, visualItem);
    }

    /**
     * Insert Items into slots
     *
     * @param inventory Inventory
     * @param stack     Item
     * @param at        index at Start
     * @param to        index End
     */
    private void insertItemBetweenIndex(Inventory inventory, ItemStack stack, int at, int to) {
        for (int index = at; index <= to; index++) {
            inventory.setItem(index, stack);
        }
    }
}
