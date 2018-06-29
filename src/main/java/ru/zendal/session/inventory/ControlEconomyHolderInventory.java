package ru.zendal.session.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.zendal.session.Session;

public class ControlEconomyHolderInventory implements InventoryHolder {

    private final Session session;

    public ControlEconomyHolderInventory(Session session){
        this.session = session;
    }


    public Session getSession() {
        return session;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
