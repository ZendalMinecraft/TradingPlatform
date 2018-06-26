package ru.zendal.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.*;
import ru.zendal.TradingPlatform;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.TradeOfflineSessionStatus;
import ru.zendal.session.exception.TradeSessionManagerException;

public class PlayerOfflineSessionEvent implements Listener {

    private final TradingPlatform plugin;

    public PlayerOfflineSessionEvent(TradingPlatform tradingPlatform) {
        this.plugin = tradingPlatform;
    }

    @EventHandler
    public void onMoveInOfflineModeSession(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (this.needCancelEvent(player)) {
            this.plugin.getAdaptiveMessage("trade.offline.onMove").sendMessage(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        TradeOfflineSession session;
        if ((session = this.getTradeOfflineSession(player)) != null) {
            event.setCancelled(true);
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                //Cancel
                if (player.getInventory().getHeldItemSlot() == 7) {
                    Bukkit.broadcastMessage("Noo");
                    //Confirm
                } else if (player.getInventory().getHeldItemSlot() == 8) {
                    session.playerReady();
                } else {
                    this.plugin.getAdaptiveMessage("trade.offline.onInteract").sendMessage(player);
                }
            } else {
                this.plugin.getAdaptiveMessage("trade.offline.onInteract").sendMessage(player);
            }
        }
    }

    @EventHandler
    public void onMoveServiceItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (this.needCancelEvent(player)) {
            if (event.isShiftClick()) {
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
                player.updateInventory();
            }
            if (event.getSlot() == 7 || event.getSlot() == 8) {
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
                player.updateInventory();
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void g(InventoryEvent event) {
        Bukkit.broadcastMessage(String.valueOf(event.getEventName()));
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (this.needCancelEvent(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (this.needCancelEvent(player)) {
            this.plugin.getAdaptiveMessage("trade.offline.onInteract").sendMessage(player);
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (this.needCancelEvent(player)) {
            this.plugin.getAdaptiveMessage("trade.offline.onInteract").sendMessage(player);
            event.setCancelled(true);
        }
    }

    /**
     * Disable achievement if user has Offline Trade
     * TODO Not work....
     *
     * @param event Event
     */
    @EventHandler
    public void onAchievementUnlock(PlayerAchievementAwardedEvent event) {
        Player player = event.getPlayer();
        if (this.needCancelEvent(player)) {
            event.setCancelled(true);
        }
    }

    private boolean needCancelEvent(Player player) {
        try {
            TradeOfflineSession session = this.plugin.getSessionManager().getOfflineSessionByPlayer(player);
            return session.getStatus() == TradeOfflineSessionStatus.SECOND_PHASE;
        } catch (TradeSessionManagerException ignore) {
            return false;
        }
    }

    private TradeOfflineSession getTradeOfflineSession(Player player) {
        try {
            TradeOfflineSession session = this.plugin.getSessionManager().getOfflineSessionByPlayer(player);
            return session;
        } catch (TradeSessionManagerException ignore) {
            return null;
        }
    }
}
