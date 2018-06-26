package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.zendal.session.inventory.CreateOfflineTradeHolderInventory;

public class TradeOfflineSession implements Session {

    private final Player player;
    private final TradeSessionCallback callback;
    private GameMode gameModePlayer;
    private Inventory inventory;
    private TradeOfflineSessionStatus status = TradeOfflineSessionStatus.FIRST_PHASE;

    private ItemStack[] playerContent;

    private ItemStack[] playerWant;

    public TradeOfflineSession(Player player,TradeSessionCallback callback) {
        this.player = player;
        this.callback = callback;
        this.initInventory();
    }

    private void initInventory() {
        //TODO change messsage to Lang
        this.inventory = Bukkit.createInventory(new CreateOfflineTradeHolderInventory(), 54, "Input your items");
        ItemStack nextViewItem = new ItemStack(Material.REDSTONE_TORCH_ON);
        this.inventory.setItem(53, nextViewItem);
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public TradeOfflineSessionStatus getStatus() {
        return status;
    }

    public void playerReady() {
        if (status == TradeOfflineSessionStatus.FIRST_PHASE) {
            this.processFirstPhase();
        } else if (status == TradeOfflineSessionStatus.SECOND_PHASE) {
            this.processSecondPhase();
        }
    }


    private void processFirstPhase() {
        status = TradeOfflineSessionStatus.SECOND_PHASE;
        playerContent = player.getInventory().getContents();
        gameModePlayer = player.getGameMode();
        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().clear();
        this.updateInventoryForSecondPhase(player.getInventory());
        player.closeInventory();
    }

    private void processSecondPhase() {
        status = TradeOfflineSessionStatus.FINISH;
        player.setGameMode(gameModePlayer);
        playerWant = player.getInventory().getContents();
        player.getInventory().clear();
        player.getInventory().setContents(playerContent);
        player.closeInventory();
        callback.onReady(this);
    }

    private void updateInventoryForSecondPhase(Inventory inventory) {
        ItemStack redWool = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemStack greenWool = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta redWoolMeta = redWool.getItemMeta();
        ItemMeta greenWoolMeta = greenWool.getItemMeta();

        //TODO add support Lang
        redWoolMeta.setDisplayName("Cancel trade");
        greenWoolMeta.setDisplayName("Confirm trade");

        redWool.setItemMeta(redWoolMeta);
        greenWool.setItemMeta(greenWoolMeta);

        inventory.setItem(8, greenWool);
        inventory.setItem(7, redWool);
    }

    public void cancelTrade() {
        player.getInventory().clear();
        player.getInventory().addItem(inventory.getContents());
        player.setGameMode(gameModePlayer);
        status = TradeOfflineSessionStatus.FIRST_PHASE;
    }

}
