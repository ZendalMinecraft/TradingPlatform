/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.event;

import org.bukkit.Bukkit;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.BroadcastMessageEvent;
import ru.zendal.TradingPlatform;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.exception.TradeSessionManagerException;

import javax.validation.MessageInterpolator;

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


    private boolean needCancelEvent(Player player) {
        try {
            TradeOfflineSession session = this.plugin.getSessionManager().getOfflineSessionByPlayer(player);
            return session.getBuyer() != null;
        } catch (TradeSessionManagerException ignore) {
            return false;
        }
    }
}
