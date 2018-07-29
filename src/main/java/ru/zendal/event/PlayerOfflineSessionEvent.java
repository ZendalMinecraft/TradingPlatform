/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.exception.TradeSessionManagerException;

public class PlayerOfflineSessionEvent implements Listener {

    private final TradeSessionManager sessionManager;
    private final LanguageConfig languageConfig;

    public PlayerOfflineSessionEvent(TradeSessionManager sessionManager, LanguageConfig languageConfig) {
        this.sessionManager = sessionManager;
        this.languageConfig = languageConfig;
    }

    @EventHandler
    public void onMoveInOfflineModeSession(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (this.needCancelEvent(player)) {
            languageConfig.getMessage("trade.offline.onMove").sendMessage(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (this.needCancelEvent(player)) {
            event.setCancelled(true);
            languageConfig.getMessage("trade.offline.onInteract").sendMessage(player);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (this.needCancelEvent(player)) {
            languageConfig.getMessage("trade.offline.onInteract").sendMessage(player);
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (this.needCancelEvent(player)) {
            if (!event.getMessage().equalsIgnoreCase("/trade create")) {
                languageConfig.getMessage("trade.offline.onCommand").sendMessage(player);
                event.setCancelled(true);
            }
        }
    }

    /**
     * Disable achievement if user has Offline Trade
     *
     * @param event Event
     */
    @EventHandler
    public void onAchievementUnlock(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        if (this.needCancelEvent(player)) {
            player.getAdvancementProgress(event.getAdvancement())
                    .revokeCriteria(event.getAdvancement().getCriteria().toArray(new String[0])[0]);
        }
    }

    /**
     * This play now in trade session mode
     *
     * @param player Player
     * @return In trade mode
     */
    private boolean needCancelEvent(Player player) {
        try {
            TradeOfflineSession session = sessionManager.getOfflineSessionByPlayer(player);
            return session.getBuyer() != null;
        } catch (TradeSessionManagerException ignore) {
            return false;
        }
    }
}
