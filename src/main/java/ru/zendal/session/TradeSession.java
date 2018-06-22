package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TradeSession {

    private final TradeSessionCallback callback;
    private Inventory inventory;

    private Player seller;

    private Player buyer;


    private boolean sellerReady = false;
    private boolean buyerReady = false;


    public TradeSession(Inventory inventory, Player seller, Player buyer, TradeSessionCallback callback) {
        this.inventory = inventory;
        this.seller = seller;
        this.buyer = buyer;
        this.callback = callback;
    }


    public Inventory getInventory() {
        return inventory;
    }

    public Player getSeller() {
        return seller;
    }

    public Player getBuyer() {
        return buyer;
    }


    public TradeSession setReadySeller(boolean ready) {
        this.sellerReady = ready;
        this.checkReadyTrade();
        return this;
    }

    public TradeSession setReadyBuyer(boolean ready) {
        this.buyerReady = ready;
        this.checkReadyTrade();
        return this;
    }

    public List<ItemStack> getSellerItems() {
        List<ItemStack> items = new ArrayList<>();
        int slot = 0;
        for (int row = 0; row < 6; row++) {
            while (!((slot + 5) % 9 == 0)) {
                ItemStack item = this.getInventory().getItem(slot);
                if (item!=null) {
                    items.add(item);
                }
                slot++;
            }
            slot += 5;
        }
        return items;
    }

    //TODO add block Trade

    public List<ItemStack> getBuyerItems() {
        List<ItemStack> items = new ArrayList<>();
        int slot = 5;
        for (int row = 0; row < 6; row++) {
            while (!(slot % 9 == 0)) {
                ItemStack item = this.getInventory().getItem(slot);
                if (item!=null) {
                    items.add(item);
                }
                slot++;
            }
            slot += 5;
        }
        return items;
    }

    /**
     *
     */
    private void checkReadyTrade() {
        if (!(this.buyerReady && this.sellerReady)) {
            return;
        }
        this.callback.onReady(this);
    }

}
