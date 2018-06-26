package ru.zendal.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.zendal.TradingPlatform;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.TradeOfflineSessionStatus;
import ru.zendal.session.exception.TradeSessionManagerException;

public class PlayerEvent implements Listener {

    private final TradingPlatform plugin;

    public PlayerEvent(TradingPlatform tradingPlatform) {
        this.plugin = tradingPlatform;
    }

    @EventHandler
    public void onMoveInOfflineModeSession(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        try {
            TradeOfflineSession session = this.plugin.getSessionManager().getOfflineSessionByUser(player);
            if (session.getStatus() == TradeOfflineSessionStatus.SECOND_PHASE) {
                event.setCancelled(true);
            }
        } catch (TradeSessionManagerException ignore) {

        }
    }
}
