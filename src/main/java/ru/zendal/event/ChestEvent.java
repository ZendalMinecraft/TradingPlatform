package ru.zendal.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
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
            //TODO added method for Detect changed Inventory
            if (event.isShiftClick()) {
                plugin.getAdaptiveMessage("trade.inventory.shift").sendMessage((Player) event.getWhoClicked());
                event.setCancelled(true);
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
                if (this.isServiceSlot(event.getSlot())) {
                    event.setCancelled(true);
                    this.changeSessionStatus(event, session);
                    return;
                } else {
                    if (this.canInteractWithSlot(event.getSlot(), (Player) event.getWhoClicked(),session)){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * Change player trade status
     *
     * @param event   Event click on Inventory
     * @param session Trade Session
     * @see InventoryClickEvent
     * @see TradeSession
     */
    private void changeSessionStatus(InventoryClickEvent event, TradeSession session) {
        if (event.getCurrentItem().getType() == Material.WOOL) {
            byte data = event.getCurrentItem().getData().getData();
            if (data == 14) {
                this.plugin.getSessionManager().cancelSession(session);
            } else if (data == 5) {
                if (session.getSeller() == event.getWhoClicked()) {
                    session.setReadySeller(!session.isSellerReady());
                } else if (session.getBuyer() == event.getWhoClicked()) {
                    session.setReadyBuyer(!session.isBuyerReady());
                }

            }
        }
    }

    /**
     * On player drag item into TradeSessionHolderInventory
     *
     * @param event Event Inventory Drag Event
     * @see TradeSessionHolderInventory
     * @see InventoryDragEvent
     */
    @EventHandler
    public void onDragEventInTrade(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof TradeSessionHolderInventory) {
            event.getRawSlots().forEach(data -> {
                if (data > 53) {
                    return;
                }
                try {
                    TradeSession session = plugin.getSessionManager().getSessionByInventory(event.getInventory());

                    if (this.canInteractWithSlot(data, (Player) event.getWhoClicked(),session)){
                        event.setCancelled(true);
                    }
                } catch (TradeSessionManagerException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private boolean canInteractWithSlot(int indexSlot,Player whoClicked, TradeSession session){
        //Check foreign slot (Buyer)
        if (isSellerSlot(indexSlot) && session.getSeller() != whoClicked) {
            return true;
        }
        //Check foreign slot (Seller)
        if (!isSellerSlot(indexSlot) && session.getSeller() == whoClicked) {
            return true;
        }

        //Check Seller is ready
        if (session.getSeller() == whoClicked && session.isSellerReady()) {
            return true;
        }

        //Check Buyer is ready
        if (session.getBuyer() == whoClicked && session.isBuyerReady()) {
            return true;
        }
        return false;
    }

    /**
     * Check is Service slot
     *
     * @param slot index slot
     * @return {@code true} if slot is service else {@code false}
     */
    private boolean isServiceSlot(int slot) {
        if ((slot + 5) % 9 == 0) {
            return true;
        }
        return slot >= 45;
    }

    /**
     * Check is slot Seller
     *
     * @param slot index slot
     * @return {@code true} if slot is seller slot else {@code false}
     */
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
