package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.zendal.session.inventory.TradeSessionHolderInventory;
import ru.zendal.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class TradeSession implements Session {

    /**
     * Callback after all Player set Status Ready
     */
    protected final TradeSessionCallback callback;
    protected Inventory inventory;

    private Player seller;

    private Player buyer;

    protected boolean sellerReady = false;
    private boolean buyerReady = false;


    public TradeSession(Player seller, Player buyer, TradeSessionCallback callback) {
        this.seller = seller;
        this.buyer = buyer;
        this.callback = callback;
        this.initInventory();
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

    public boolean isSellerReady() {
        return sellerReady;
    }

    public boolean isBuyerReady() {
        return buyerReady;
    }

    public TradeSession setReadyBuyer(boolean ready) {
        this.buyerReady = ready;
        this.checkReadyTrade();
        return this;
    }

    public List<ItemStack> getSellerItems() {
        List<ItemStack> items = new ArrayList<>();
        int slot = 0;
        for (int row = 0; row < 5; row++) {
            while (!((slot + 5) % 9 == 0)) {
                ItemStack item = this.getInventory().getItem(slot);
                if (item != null) {
                    items.add(item);
                }
                slot++;
            }
            slot += 5;
        }
        return items;
    }

    protected void setSeller(Player seller) {
        this.seller = seller;
    }


    protected void setBuyer(Player buyer) {
        this.buyer = buyer;
    }

    /**
     * Initialize inventory for trade between players
     */
    private void initInventory() {
        this.createInventory();
        ItemStack stick = new ItemStack(Material.STICK);
        for (int i = 0; i < 6; i++) {
            inventory.setItem(9 * i + 4, stick);
        }

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);


        this.insertItemBetweenIndex(inventory, glass, 45, 53);

        ItemStack redWool = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemStack greenWool = new ItemStack(Material.WOOL, 1, (short) 5);

        inventory.setItem(9 * 4 + 4, redWool);
        inventory.setItem(9 * 1 + 4, greenWool);
        inventory.setItem(9 * 5 + 4, stick);
    }

    protected void createInventory() {
        inventory = Bukkit.createInventory(new TradeSessionHolderInventory(), 9 * 6, this.getTitleForInventoryTrade());
    }


    /**
     * Change title inventory for users
     */
    protected void changeTitleInventory(String title) {
        Inventory newInventory = Bukkit.createInventory(inventory.getHolder(), inventory.getSize(), title);
        for (int index = 0; index < inventory.getSize(); index++) {
            ItemStack itemStack = inventory.getItem(index);
            if (itemStack != null) {
                newInventory.setItem(index, itemStack);
            }
        }

        if (buyer.getOpenInventory().getTopInventory().hashCode() == inventory.hashCode())
            buyer.openInventory(newInventory);
        if (seller.getOpenInventory().getTopInventory().hashCode() == inventory.hashCode())
            seller.openInventory(newInventory);

        inventory = newInventory;
    }

    public List<ItemStack> getBuyerItems() {
        List<ItemStack> items = new ArrayList<>();
        int slot = 5;
        for (int row = 0; row < 5; row++) {
            while (!(slot % 9 == 0)) {
                ItemStack item = this.getInventory().getItem(slot);
                if (item != null) {
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
    protected void checkReadyTrade() {

        this.changeVisualStatusTrade();
        this.changeTitleInventory(this.getTitleForInventoryTrade());
        if (!(this.buyerReady && this.sellerReady)) {
            return;
        }
        this.callback.onReady(this);
    }


    public void enableTimer(JavaPlugin plugin) {
        TradeSession self = this;
        new BukkitRunnable() {
            private int timerStart = 35;
            private double couf = 1561/timerStart;

            @Override
            public void run() {
                ItemStack  stick =  ItemBuilder.get(Material.DIAMOND_SWORD).setDurability((short) (couf*timerStart)).build();
               // ItemStack stick = new ItemStack(Material.STICK, timerStart);
                for (int i = 0; i < 6; i++) {
                    if (i != 1 && i != 4) {
                        inventory.setItem(9 * i + 4, stick);
                    }
                }
                timerStart--;
                if (timerStart==-1){
                   if (isBuyerReady() && isSellerReady()){
                       callback.processTrade(self);
                   }
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin,10L,1L);
    }

    /**
     * Change Visual status (change glass color on last line) in Inventory
     */
    private void changeVisualStatusTrade() {
        ItemStack notReadyVisualStatus = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemStack readyVisualStatus = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);

        ItemStack visualStatus = this.sellerReady ? readyVisualStatus : notReadyVisualStatus;
        this.insertItemBetweenIndex(this.inventory, visualStatus, 45, 48);


        visualStatus = this.buyerReady ? readyVisualStatus : notReadyVisualStatus;
        this.insertItemBetweenIndex(this.inventory, visualStatus, 50, 53);
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


    /**
     * Get title for Inventory trade
     *
     * @return Title
     */
    protected String getTitleForInventoryTrade() {
        StringBuilder titleInventory = new StringBuilder();
        titleInventory.append(seller.getDisplayName()).append("(").append(this.sellerReady ? "✔" : "×").append(")");

        StringBuilder subTitleInventory = new StringBuilder(buyer.getDisplayName());
        subTitleInventory.append("(").append(this.buyerReady ? "✔" : "×").append(")");

        int countSpace = 36 - titleInventory.length() - subTitleInventory.length();
        while (--countSpace > 0) {
            titleInventory.append(" ");
        }
        titleInventory.append(subTitleInventory);
        return titleInventory.toString();
    }

}
