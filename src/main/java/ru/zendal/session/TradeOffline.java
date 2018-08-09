/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.service.economy.EconomyProvider;
import ru.zendal.session.inventory.holder.ViewOfflineTradeHolderInventory;
import ru.zendal.util.ItemBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This type session used
 */
public class TradeOffline {

    /**
     * Player who create this trade (Offline)
     *
     * @see OfflinePlayer
     */
    private final OfflinePlayer player;

    /**
     * Items owner trade
     *
     * @see ItemStack
     */
    private final List<ItemStack> has;

    /**
     * Items which wants to trade
     *
     * @see ItemStack
     */
    private final List<ItemStack> wants;

    /**
     * Unique ID trade
     */
    private final String uniqueId;

    /**
     * Inventory for accept trade
     *
     * @see Inventory
     */
    private Inventory inventory;

    /**
     * Bet has
     */
    private double betHas;


    /**
     * Bet want
     */
    private double betWant;

    /**
     * Constructor Trade Offline
     *
     * @param uniqueId Unique ID
     * @param player   Player who create this trade
     * @param has      Items owner trade
     * @param wants    items which wants to trade
     * @see ItemStack
     * @see Player
     */
    public TradeOffline(String uniqueId, OfflinePlayer player, List<ItemStack> has, List<ItemStack> wants) {
        this.uniqueId = uniqueId;
        this.player = player;
        this.has = has;
        this.wants = wants;
    }

    public TradeOffline setBetHas(double bet) {
        this.betHas = bet;
        return this;
    }

    public TradeOffline setBetWant(double bet) {
        this.betWant = bet;
        return this;
    }

    /**
     * Get owner trade
     *
     * @return owner trade
     * @see OfflinePlayer
     */
    public OfflinePlayer getOfflinePlayer() {
        return player;
    }

    /**
     * Get list items owner trade
     *
     * @return Items owner trade
     */
    public List<ItemStack> getHas() {
        return has;
    }

    /**
     * Get list items which wants to trade
     *
     * @return items which wants to trade
     */
    public List<ItemStack> getWants() {
        return wants;
    }

    /**
     * Get inventory for accept trade
     *
     * @return Inventory for accept trade
     */
    public synchronized Inventory getInventory() {
        if (inventory == null) {
            inventory = this.generateInventory();
        }
        return inventory;
    }

    /**
     * Get unique ID trade
     *
     * @return Unique ID trade
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * Confirm this trade
     *
     * @param player Player who accept this trade
     * @param economyProvider Instance economy Provider
     * @return A list of missing items. List can be empty.
     * @see Player
     */
    public TradeOfflineConfirmResponse confirmTrade(Player player, EconomyProvider economyProvider) {
        List<ItemStack> listMissingItems = new ArrayList<>();

        List<ItemStack> needItems = this.cloneListItemStack(this.getWants());
        List<ItemStack> hasItems = this.cloneListItemStack(
                Arrays.asList(player.getInventory().getContents().clone())
        );

        for (ItemStack needItemStack : needItems) {
            for (ItemStack hasItemStack : hasItems) {
                if (hasItemStack != null) {
                    this.subtractItemStack(needItemStack, hasItemStack);
                }
            }
        }


        for (ItemStack item : needItems) {
            if (item.getAmount() > 0) {
                listMissingItems.add(item);
            }
        }
        double needMoney = 0;
        if (!economyProvider.haveMoney(player, betWant)) {
            needMoney = betWant - economyProvider.getBalance(player);
        }
        return new TradeOfflineConfirmResponse(listMissingItems, hasItems, needMoney);
    }

    /**
     * Subtract itemsStack
     *
     * @param minuend  Minuend ItemStack
     * @param deducted Deducted ItemStack
     * @see ItemStack
     */
    private void subtractItemStack(ItemStack minuend, ItemStack deducted) {
        if (!isEqualsItemStack(minuend, deducted) || deducted.getAmount() == 0) {
            return;
        }
        if (minuend.getAmount() < deducted.getAmount()) {
            deducted.setAmount(deducted.getAmount() - minuend.getAmount());
            minuend.setAmount(0);
        } else {
            minuend.setAmount(minuend.getAmount() - deducted.getAmount());
            deducted.setAmount(0);
        }
    }

    /**
     * Equals two ItemStack
     *
     * @param firstItemStack  First ItemStack to equals
     * @param secondItemStack Second ItemStack to equals
     * @return {@code true} if ItemStacks equal else {@code false}
     * @see ItemStack
     */
    private boolean isEqualsItemStack(ItemStack firstItemStack, ItemStack secondItemStack) {
        if (firstItemStack.getType() != secondItemStack.getType()) {
            return false;
        }

        if (firstItemStack.getData().getData() != secondItemStack.getData().getData()) {
            return false;
        }

        for (Map.Entry<Enchantment, Integer> entry : firstItemStack.getEnchantments().entrySet()) {
            Integer levelEnchantment = secondItemStack.getEnchantments().get(entry.getKey());
            if (levelEnchantment == null || !levelEnchantment.equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generate Inventory for trade
     *
     * @return Inventory to trade
     * @see Inventory
     */
    private Inventory generateInventory() {
        Inventory inventory = Bukkit.createInventory(new ViewOfflineTradeHolderInventory(), 54);
        this.addedItemsToInventory(inventory);
        int indexSlot = 4;
        ItemStack stick = ItemBuilder.get(Material.STICK).build();
        for (int indexLine = 0; indexLine < 6; indexLine++) {
            inventory.setItem(indexSlot, stick);
            indexSlot += 9;
        }
        for (indexSlot = 45; indexSlot < 54; indexSlot++) {
            inventory.setItem(indexSlot, stick);
        }
        inventory.setItem(9 * 1 + 4, ItemBuilder.get(Material.WOOL).setDurability((short) 5).build());
        inventory.setItem(9 * 4 + 4, ItemBuilder.get(Material.WOOL).setDurability((short) 14).build());
        inventory.setItem(9 * 5 + 4,
                ItemBuilder
                        .get(Material.GOLD_NUGGET)
                        .setDisplayName("Amount").setItemLore(Arrays.asList(
                        "Bet " + player.getName() + ": " + betHas,
                        "Bet need: " + betWant
                )).build()
        );
        return inventory;
    }

    /**
     * Add items to trade into Inventory
     *
     * @param inventory Inventory
     * @see Inventory
     */
    private void addedItemsToInventory(Inventory inventory) {
        int indexSlot = 0;
        for (ItemStack itemStack : has) {
            inventory.setItem(indexSlot, itemStack);
            if ((indexSlot + 6) % 9 == 0) {
                indexSlot += 5;
            }
            indexSlot++;
        }
        indexSlot = 5;
        for (ItemStack itemStack : wants) {
            inventory.setItem(indexSlot, itemStack);
            if ((indexSlot + 1) % 9 == 0) {
                indexSlot += 5;
            }
            indexSlot++;
        }
    }


    private List<ItemStack> cloneListItemStack(List<ItemStack> itemStackList) {
        List<ItemStack> newStack = new ArrayList<>();
        for (ItemStack itemStack : itemStackList) {
            if (itemStack != null) {
                newStack.add(itemStack.clone());
            } else {
                //This need for correct set Contents
                newStack.add(null);
            }
        }
        return newStack;
    }

    /**
     * Factory for create TradeOffline by TradeOfflineSession
     *
     * @param uniqueId Unique ID trade
     * @param session  TradeOfflineSession
     * @return instance TradeOffline
     * @see TradeOfflineSession
     */
    public static TradeOffline factory(String uniqueId, TradeOfflineSession session) {
        return new TradeOffline(uniqueId, session.getBuyer(), session.getSellerItems(), session.getBuyerItems())
                .setBetWant(session.getBetBuyer())
                .setBetHas(session.getBetSeller());
    }
}
