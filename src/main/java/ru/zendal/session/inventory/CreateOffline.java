package ru.zendal.session.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.zendal.TradeSessionHolderInventory;

public class CreateOffline extends TradeSessionHolderInventory implements InventoryHolder {
    @Override
    public Inventory getInventory() {
        return null;
    }
}
