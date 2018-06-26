package ru.zendal.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import ru.zendal.TradingPlatform;
import ru.zendal.session.TradeOfflineSession;
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
        if (this.needCancelEvent(player)) {
            event.setCancelled(true);
            this.plugin.getAdaptiveMessage("trade.offline.onInteract").sendMessage(player);
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
            if (!event.getMessage().equalsIgnoreCase("/trade create")) {
                this.plugin.getAdaptiveMessage("trade.offline.onCommand").sendMessage(player);
                event.setCancelled(true);
            }
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
            return session.getBuyer() != null;
        } catch (TradeSessionManagerException ignore) {
            return false;
        }
    }
}
