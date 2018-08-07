/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.session.inventory.CreateOfflineTradeHolderInventory;
import ru.zendal.util.ItemBuilder;

import java.util.Arrays;


/**
 * This is a normal trade Session, only here is the emulation of 2 users
 */
public class TradeOfflineSession extends TradeSession {

    /**
     * Initial gamemode
     *
     * @see GameMode
     */
    private GameMode gameModePost;

    /**
     * Initial items
     *
     * @see ItemStack
     */
    private ItemStack[] inventoryPost;


    /**
     * Constructor TradeOfflineSession
     *
     * @param seller   Player created trade
     * @param callback Callback's
     * @see Player
     * @see TradeSessionCallback
     */
    public TradeOfflineSession(Player seller, TradeSessionCallback callback) {
        super(seller, null, callback);
        getSeller().openInventory(getInventory());
    }


    @Override
    protected void createInventory() {
        inventory = Bukkit.createInventory(new CreateOfflineTradeHolderInventory(), 9 * 6, this.getTitleForInventoryTrade());
    }


    @Override
    public TradeOfflineSession setReadySeller(boolean ready) {
        this.sellerReady = ready;
        if (ready) {
            this.checkReadyTrade();
            this.givePlayerCreative();
            setBuyer(getSeller());
            setSeller(null);
        }
        return this;
    }

    /**
     * Give player creative mode
     */
    private void givePlayerCreative() {
        if (getSeller() != null) {
            inventoryPost = getSeller().getInventory().getContents();
            gameModePost = getSeller().getGameMode();
            getSeller().setGameMode(GameMode.CREATIVE);
            getSeller().getInventory().clear();
        }
    }

    @Override
    protected void checkReadyTrade() {
        super.checkReadyTrade();
    }

    @Override
    protected void onTimerEnd() {
        if (isBuyerReady() && isSellerReady()) {
            callback.processTrade(this);
            rollBackPlayer();
        }
    }

    @Override
    protected String getTitleForInventoryTrade() {
        StringBuilder titleInventory = new StringBuilder();
        titleInventory.append("Your items").append("(").append(this.isSellerReady() ? "✔" : "×").append(")");

        StringBuilder subTitleInventory = new StringBuilder("Creative");
        subTitleInventory.append("(").append(this.isBuyerReady() ? "✔" : "×").append(")");
        int countSpace = 36 - titleInventory.length() - subTitleInventory.length() +
                this.getCountServiceSymbols(titleInventory.toString()) * 2 +
                this.getCountServiceSymbols(subTitleInventory.toString()) * 2;
        while (--countSpace > 0) {
            titleInventory.append(" ");
        }
        titleInventory.append(subTitleInventory);
        return titleInventory.toString();
    }

    @Override
    protected void changeTitleInventory(String title) {
        Inventory newInventory = Bukkit.createInventory(inventory.getHolder(), inventory.getSize(), title);
        for (int index = 0; index < inventory.getSize(); index++) {
            ItemStack itemStack = inventory.getItem(index);
            if (itemStack != null) {
                newInventory.setItem(index, itemStack);
            }
        }
        if (getSeller() != null && getSeller().getOpenInventory().getTopInventory().hashCode() == inventory.hashCode())
            getSeller().openInventory(newInventory);

        if (getBuyer() != null && getBuyer().getOpenInventory().getTopInventory().hashCode() == inventory.hashCode())
            getBuyer().openInventory(newInventory);
        inventory = newInventory;
    }


    @Override
    protected ItemBuilder getVisualDisplayBet() {
        ItemBuilder itemBuilder = ItemBuilder.get(Material.GOLD_NUGGET);
        itemBuilder.setDisplayName("Amount");
        itemBuilder.setItemLore(Arrays.asList(
                "Bet Your bet: " + getBetSeller(),
                "Bet Creative: " + getBetBuyer())
        );
        return itemBuilder;
    }

    /**
     * Roll back the player to the initial state
     */
    private void rollBackPlayer() {
        Player player = getSeller() != null ? getSeller() : getBuyer();
        if (inventoryPost != null) {
            player.getInventory().clear();
            player.getInventory().setContents(inventoryPost);
            player.setGameMode(gameModePost);
        }
        player.closeInventory();
    }

    /**
     * Cancel trade
     */
    public void cancelTrade() {
        this.rollBackPlayer();
        Player player = getSeller() != null ? getSeller() : getBuyer();
        player.getInventory().addItem(getSellerItems().toArray(new ItemStack[0]));
        setReadySeller(false);
        setReadyBuyer(false);
        player.closeInventory();
        player.updateInventory();
    }
}
