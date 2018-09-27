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
import org.bukkit.inventory.Inventory;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.exception.TradeSessionManagerException;

/**
 * Command processor
 * <p>
 * /trade storage
 */
public class StorageCommandProcessor implements ArgsCommandProcessor {

    /**
     * Instance TradeSessionManager
     */
    private final TradeSessionManager sessionManager;
    /**
     * Instance LanguageConfig
     */
    private final LanguageConfig languageConfig;

    /**
     * Constructor
     *
     * @param sessionManager Instance TradeSessionManager
     * @param languageConfig Instance LanguageConfig
     */
    public StorageCommandProcessor(TradeSessionManager sessionManager, LanguageConfig languageConfig) {
        this.sessionManager = sessionManager;
        this.languageConfig = languageConfig;
    }

    @Override
    public boolean process(Command command, CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    languageConfig.getMessage("command.storage.error.executorNotPlayer").toString()
            );
        } else {
            this.openStorageForPlayer((Player) sender);
        }
        return true;
    }

    /**
     * Open storage for Player
     *
     * @param player Player
     */
    private void openStorageForPlayer(Player player) {
        try {
            Inventory inventory = this.sessionManager.getInventoryStorageByPlayer(player);
            player.openInventory(inventory);
        } catch (TradeSessionManagerException e) {
            this.languageConfig.getMessage("command.storage.message.isClear").sendMessage(player);
        }
    }

    @Override
    public boolean isCanBeProcessed(CommandSender sender, String[] args) {
        return args.length > 0 && args[0].equalsIgnoreCase("storage");
    }
}
