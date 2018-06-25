package ru.zendal.event;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import ru.zendal.TradingPlatform;
import ru.zendal.session.inventory.StorageHolderInventory;

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
            if (event.getClickedInventory() instanceof StorageHolderInventory){
                event.setCancelled(true);
            }
            if (event.isShiftClick()){
                event.setCancelled(true);
            }
        }
    }

}
