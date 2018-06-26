package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.session.inventory.CreateOffline;


/**
 * This is a normal trade Session, only here is the emulation of 2 users
 */
public class TradeOfflineSession extends TradeSession {


    private GameMode gameModePost;

    private ItemStack[] inventoryPost;


    public TradeOfflineSession(Player seller, TradeSessionCallback callback) {
        super(seller, null, callback);
        getSeller().openInventory(getInventory());
    }


    @Override
    protected void createInventory() {
        inventory = Bukkit.createInventory(new CreateOffline(), 9 * 6, this.getTitleForInventoryTrade());
    }


    @Override
    public TradeOfflineSession setReadySeller(boolean ready) {
        this.sellerReady = ready;
        this.checkReadyTrade();
        this.givePlayerCreative();
        setBuyer(getSeller());
        setSeller(null);
        return this;
    }

    private void givePlayerCreative() {
        inventoryPost = getSeller().getInventory().getContents();
        gameModePost = getSeller().getGameMode();
        getSeller().setGameMode(GameMode.CREATIVE);
        getSeller().getInventory().clear();
    }

    @Override
    protected void checkReadyTrade() {
        super.checkReadyTrade();
        if (this.isBuyerReady() && this.isSellerReady()) {
            this.rollBackPlayer();
        }
    }

    @Override
    protected String getTitleForInventoryTrade() {
        StringBuilder titleInventory = new StringBuilder();
        titleInventory.append("Your items").append("(").append(this.isSellerReady() ? "✔" : "×").append(")");

        StringBuilder subTitleInventory = new StringBuilder("Creative");
        subTitleInventory.append("(").append(this.isBuyerReady() ? "✔" : "×").append(")");

        int countSpace = 36 - titleInventory.length() - subTitleInventory.length();
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


    private void rollBackPlayer() {
        Player player = getSeller() != null ? getSeller() : getBuyer();
        if (inventoryPost != null) {
            player.getInventory().clear();
            player.getInventory().setContents(inventoryPost);
            player.setGameMode(gameModePost);
        }
        player.closeInventory();
    }

    public void cancelTrade() {
        this.rollBackPlayer();
        Player player = getSeller() != null ? getSeller() : getBuyer();
        player.getInventory().addItem(getSellerItems().toArray(new ItemStack[0]));
        player.closeInventory();
    }
}
