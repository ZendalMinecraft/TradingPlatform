package ru.zendal.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.TradeOfflineSessionStatus;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.exception.TradeSessionManagerException;
import ru.zendal.session.inventory.CreateOfflineTradeHolderInventory;

public class ChestOfflineSessionEvent implements Listener {

    private final TradeSessionManager manager;

    public ChestOfflineSessionEvent(TradeSessionManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onInteractEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getHolder() instanceof CreateOfflineTradeHolderInventory) {
            //On pick item-ready
            try {
                TradeOfflineSession session = manager.getOfflineSessionByPlayer((Player) event.getWhoClicked());
                if (event.getSlot() == 53 && session.getStatus()==TradeOfflineSessionStatus.FIRST_PHASE) {
                    event.setCancelled(true);
                    session.playerReady();

                }
            } catch (TradeSessionManagerException e) {
                e.printStackTrace();
            }
        }

    }
}
