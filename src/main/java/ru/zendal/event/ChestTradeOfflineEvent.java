/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.TradingPlatform;
import ru.zendal.session.TradeOffline;
import ru.zendal.session.exception.TradeSessionManagerException;
import ru.zendal.session.inventory.ViewOfflineTradeHolderInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChestTradeOfflineEvent implements Listener {


    private final TradingPlatform tradingPlatform;

    public ChestTradeOfflineEvent(TradingPlatform tradingPlatform) {
        this.tradingPlatform = tradingPlatform;
    }

    @EventHandler
    public void onChest(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (inventory.getHolder() instanceof ViewOfflineTradeHolderInventory) {
            event.setCancelled(true);
            if (event.getSlot() == 9 + 4) {
                try {
                    this.process(player, tradingPlatform.getSessionManager().getTradeOfflineByInventory(inventory));
                } catch (TradeSessionManagerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void process(Player player, TradeOffline tradeOffline) {
        List<ItemStack> itemsNeed = this.cloneListItemStack(tradeOffline.getWants());
        List<ItemStack> itemsHas = this.cloneListItemStack(Arrays.asList(player.getInventory().getContents().clone()));

        for (ItemStack itemStackNeed : itemsNeed) {
            for (ItemStack itemStackHas : itemsHas) {
                if (itemStackHas != null) {
                    if (itemStackHas.getType() == itemStackNeed.getType() && itemStackHas.getAmount() != 0) {
                        if (itemStackNeed.getAmount() < itemStackHas.getAmount()) {
                            itemStackHas.setAmount(itemStackHas.getAmount() - itemStackNeed.getAmount());
                            itemStackNeed.setAmount(0);
                        } else {
                            itemStackNeed.setAmount(itemStackNeed.getAmount() - itemStackHas.getAmount());
                            itemStackHas.setAmount(0);
                        }
                    }
                }
            }
        }
        boolean good = true;
        for (ItemStack itemStackNeed : itemsNeed) {
            Bukkit.broadcastMessage(itemStackNeed.toString());
            if (itemStackNeed.getAmount() > 0) {
                good = false;
                player.sendMessage(itemStackNeed.getType() + "x" + itemStackNeed.getAmount());
            }
        }
        if (good) {
            Bukkit.broadcastMessage("Yes");
            player.getInventory().clear();
            player.getInventory().setContents(itemsHas.toArray(new ItemStack[0]));
            player.getInventory().addItem(tradeOffline.getHas().toArray(new ItemStack[0]));
        }
    }

    private List<ItemStack> cloneListItemStack(List<ItemStack> itemStacks) {
        List<ItemStack> newStack = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null) {
                newStack.add(itemStack.clone());
            }
        }
        return newStack;
    }

}
