/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.TradingPlatform;
import ru.zendal.session.inventory.holder.StorageHolderInventory;

import java.util.ArrayList;
import java.util.List;

public class ChestStorageEvent implements Listener {

    private final TradingPlatform tradingPlatform;

    //TODO доделать
    public ChestStorageEvent(TradingPlatform plugin) {
        this.tradingPlatform = plugin;
    }


    @EventHandler
    public void onDragItems(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof StorageHolderInventory) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof StorageHolderInventory) {
            Inventory clickedInventory = event.getClickedInventory();
            if (clickedInventory == null) {
                return;
            }
            if (clickedInventory.getHolder() instanceof StorageHolderInventory) {
                event.setCancelled(true);
                if (event.getSlot() == 53) {
                    this.getAllItemsToPlayer(clickedInventory, (Player) event.getWhoClicked());
                }
            }
            if (event.isShiftClick()) {
                event.setCancelled(true);
            }

        }
    }

    private void getAllItemsToPlayer(Inventory inventory, Player player) {
        List<ItemStack> notFitItems = new ArrayList<>();
        for (int index = 0; index < 53; index++) {
            ItemStack stack = inventory.getItem(index);
            if (stack != null) {
                inventory.clear(index);
                player.getInventory().addItem(stack).forEach((indexItem, item) -> {
                    notFitItems.add(item);
                });
            }
        }
        inventory.addItem(notFitItems.toArray(new ItemStack[0]));
    }

}
