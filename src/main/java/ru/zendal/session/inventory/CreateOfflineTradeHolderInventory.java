package ru.zendal.session.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CreateOfflineTradeHolderInventory extends TradeSessionHolderInventory implements InventoryHolder {
    @Override
    public Inventory getInventory() {
        return null;
    }
}
