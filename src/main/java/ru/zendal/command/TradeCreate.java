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
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.exception.TradeSessionManagerException;

public class TradeCreate implements ArgsCommandProcessor {


    private final TradeSessionManager manager;

    public TradeCreate(TradeSessionManager manager, LanguageConfig languageConfig) {
        this.manager = manager;
    }

    @Override
    public boolean process(Command command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        try {
            player.openInventory(manager.getOfflineSessionByPlayer(player).getInventory());
        } catch (TradeSessionManagerException e) {
            manager.createOfflineSession(player);
        }
        return true;
    }

    @Override
    public boolean isCanBeProcessed(CommandSender sender, String[] args) {
        return args.length >= 1 && args[0].equalsIgnoreCase("create");
    }
}
