/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.zendal.config.AdaptiveMessage;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.Session;
import ru.zendal.session.inventory.holder.PickBetHolderInventory;
import ru.zendal.util.ItemBuilder;

import java.util.List;

public class PickBetInventoryManager implements InventoryManager {

    private Inventory inventory;
    private final List<Double> betSpread;

    private final LanguageConfig languageConfig;

    private final Session session;
    /**
     * offset size in inventory
     */
    private final int offsetItems;

    public PickBetInventoryManager(Session session, Player whoClicked, LanguageConfig config, List<Double> betSpread) {
        this.session = session;
        this.betSpread = betSpread;
        this.languageConfig = config;
        this.offsetItems = 4 - betSpread.size() / 2;
        this.inventory = this.createPickBetInventory(session, whoClicked);
    }

    private Inventory createPickBetInventory(Session session, Player whoClicked) {
        //TODO Append Inventory
        Inventory inventory = Bukkit.createInventory(
                new PickBetHolderInventory(this, session),
                18,
                "Amount: " + ((session.getBuyer() == whoClicked) ? session.getBetBuyer() : session.getBetSeller())
        );
        this.fillInventory(inventory);
        return inventory;
    }

    private void fillInventory(Inventory inventory) {
        this.fillBetItems(inventory,offsetItems, Material.GOLD_NUGGET, "inventory.trade.item.addBet");
        this.fillBetItems(inventory,offsetItems + 9, Material.IRON_NUGGET, "inventory.trade.item.DisBet");
    }

    private void fillBetItems(Inventory inventory, int startSlot, Material material, String messagePath) {
        ItemBuilder itemBuilder = ItemBuilder.get(material);
        for (int indexSlot = startSlot; indexSlot < startSlot + betSpread.size(); indexSlot++) {
            inventory.setItem(indexSlot,
                    this.setBetItemDisplayName(
                            itemBuilder,
                            languageConfig.getMessage(messagePath),
                            indexSlot - startSlot
                    ).build()
            );
        }
    }

    private ItemBuilder setBetItemDisplayName(ItemBuilder itemBuilder, AdaptiveMessage adaptiveMessage, int indexBet) {
        return itemBuilder.setDisplayName(
                adaptiveMessage.setCustomMessage(1, String.valueOf(betSpread.get(indexBet))).toString()
        );
    }


    public Inventory getInventory() {
        return inventory;
    }


    public double getAmountBySlotIndex(int indexSlot) {
        if (indexSlot >= 9) {
            return -1 * betSpread.get(indexSlot - offsetItems - 9);
        }
        return betSpread.get(indexSlot - offsetItems);
    }


    public void updateInventory(Player player) {
        this.inventory = this.createPickBetInventory(session, player);
        player.openInventory(inventory);
        player.updateInventory();
    }

}
