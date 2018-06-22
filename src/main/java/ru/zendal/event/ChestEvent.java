package ru.zendal.event;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import ru.zendal.TradeSessionHolderInventory;
import ru.zendal.TradingPlatform;
import ru.zendal.session.TradeSession;
import ru.zendal.session.exception.TradeSessionManagerException;

public class ChestEvent implements Listener {


    private final TradingPlatform plugin;

    public ChestEvent(TradingPlatform instance) {
        this.plugin = instance;
    }


    @EventHandler
    public void onChestMoveItem(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof TradeSessionHolderInventory) {
            if (event.getClickedInventory() == null) {
                return;
            }
            if (!event.getClickedInventory().getName().equalsIgnoreCase("container.inventory")) {
                TradeSession session;
                try {
                    session = plugin.getSessionManager().getSessionByInventory(inventory);
                } catch (TradeSessionManagerException e) {
                    e.printStackTrace();
                    return;
                }
                if ((event.getSlot() + 5) % 9 == 0) {
                    event.setCancelled(true);
                    this.changeSessionStatus(event, session);
                    return;
                } else {
                    if (isSellerSlot(event.getSlot()) && session.getSeller() != event.getWhoClicked()) {
                        event.setCancelled(true);
                        return;
                    }
                    if (!isSellerSlot(event.getSlot()) && session.getSeller() == event.getWhoClicked()) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    private void changeSessionStatus(InventoryClickEvent event, TradeSession session) {
        if (event.getCurrentItem().getType() == Material.WOOL) {
            byte data = event.getCurrentItem().getData().getData();
            if (data == 14) {
                this.plugin.getSessionManager().cancelSession(session);
            } else if (data == 5) {
                if (session.getSeller() == event.getWhoClicked()) {
                    session.setReadySeller(true);
                } else if (session.getBuyer() == event.getWhoClicked()) {
                    session.setReadyBuyer(true);
                }

            }
        }
    }

    @EventHandler
    public void onDragEventInTrade(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof TradeSessionHolderInventory) {
            event.getRawSlots().forEach(data -> {
                if (data>53){
                    return;
                }
                try {
                    TradeSession session = plugin.getSessionManager().getSessionByInventory(event.getInventory());

                    if (isSellerSlot(data) && session.getSeller() != event.getWhoClicked()) {
                        event.setCancelled(true);
                        return;
                    }
                    if (!isSellerSlot(data) && session.getSeller() == event.getWhoClicked()) {
                        event.setCancelled(true);
                        return;
                    }
                } catch (TradeSessionManagerException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private boolean isSellerSlot(int slot) {
        for (int index = 0; index < 5; index++) {
            if ((slot + 5) % 9 == 0) {
                return true;
            }
            slot++;
        }
        return false;
    }
}
