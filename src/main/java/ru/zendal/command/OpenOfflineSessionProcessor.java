/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.TradeOffline;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.exception.TradeSessionManagerException;

/**
 * Open offline trade Command processor
 * <p>
 * /trade open
 */
public class OpenOfflineSessionProcessor implements ArgsCommandProcessor {

    /**
     * Instance LanguageConfig
     */
    private final LanguageConfig language;
    /**
     * Instance TradeSessionManager
     */
    private final TradeSessionManager manager;

    /**
     * Constructor
     *
     * @param manager  Instance TradeSessionManager
     * @param language Instance LanguageConfig
     */
    public OpenOfflineSessionProcessor(TradeSessionManager manager, LanguageConfig language) {
        this.manager = manager;
        this.language = language;

    }

    @Override
    public boolean process(Command command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            language.getMessage("trade.offline.notPickId").sendMessage(player);
        } else {
            this.openTradeOffline(player, args[1]);
        }
        return true;
    }

    /**
     * Get trade and open inventory for player
     *
     * @param player Player
     * @param refId  Unique Id trade
     */
    private void openTradeOffline(Player player, String refId) {
        try {
            TradeOffline tradeOffline = manager.getTradeOfflineById(refId);
            player.openInventory(tradeOffline.getInventory());
        } catch (TradeSessionManagerException e) {
            language.getMessage("trade.offline.udefinedId").setCustomMessage(1, refId).
                    sendMessage(player);
        }
    }


    @Override
    public boolean isCanBeProcessed(CommandSender sender, String[] args) {
        return args.length > 0 && args[0].equalsIgnoreCase("open");

    }
}
