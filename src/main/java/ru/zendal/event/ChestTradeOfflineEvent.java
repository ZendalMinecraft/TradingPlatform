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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.TradeOffline;
import ru.zendal.session.TradeOfflineConfirmResponse;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.exception.TradeSessionManagerException;
import ru.zendal.session.inventory.ViewOfflineTradeHolderInventory;

/**
 * Event for view Offline Session
 */
public class ChestTradeOfflineEvent implements Listener {

    private final TradeSessionManager sessionManager;
    private final LanguageConfig languageConfig;

    public ChestTradeOfflineEvent(TradeSessionManager sessionManager, LanguageConfig languageConfig) {
        this.sessionManager = sessionManager;
        this.languageConfig = languageConfig;
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
                    this.process(player, sessionManager.getTradeOfflineByInventory(inventory));
                } catch (TradeSessionManagerException e) {
                    player.closeInventory();
                    languageConfig.getMessage("trade.offline.alreadyFinished").sendMessage(player);
                }
            } else if (event.getSlot() == 9 * 4 + 4) {
                player.closeInventory();
            }
        }
    }

    private void process(Player player, TradeOffline tradeOffline) {
        //TODO Move to SessionManager
        TradeOfflineConfirmResponse response = tradeOffline.confirmTrade(player);
        if (!response.hasMissingItems()) {
            try {
                sessionManager.removeTradeOffline(tradeOffline);
                player.getInventory().clear();
                player.getInventory().setContents(response.getNewContent());
                sessionManager.processTradeOffline(player, tradeOffline);
            } catch (TradeSessionManagerException e) {
                languageConfig.getMessage("trade.offline.alreadyFinished").sendMessage(player);
            }
        } else {
            player.closeInventory();
            player.sendMessage("Вам не хватает:");
            response.getListMissingItems().forEach(itemStack -> {
                player.sendMessage(itemStackToString(itemStack));
            });
        }
    }

    private String itemStackToString(ItemStack itemStack) {
        StringBuilder description = new StringBuilder();
        description.append(itemStack.getType().name()).append("x").append(itemStack.getAmount());
        description.append(" With enchants:");
        itemStack.getEnchantments().forEach((enchantment, level) -> {
            description.append(enchantment.getName()).append(" ").append(level).append(" ");
        });
        return description.toString();
    }

}
