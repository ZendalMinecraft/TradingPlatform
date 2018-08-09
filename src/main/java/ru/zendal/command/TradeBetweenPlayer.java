/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.TradeSession;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.exception.TradeSessionManagerException;

/**
 * Command processor
 * <p>
 * /trade to
 */
public class TradeBetweenPlayer implements ArgsCommandProcessor {

    /**
     * Instance TradeSessionManager
     */
    private final TradeSessionManager tradeSessionManager;
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
    public TradeBetweenPlayer(TradeSessionManager sessionManager, LanguageConfig languageConfig) {
        this.tradeSessionManager = sessionManager;
        this.languageConfig = languageConfig;
    }

    @Override
    public boolean process(Command command, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    languageConfig
                            .getMessage("command.to.error.executorNotPlayer")
                            .toString()
            );
        } else if (args.length < 2) {
            sender.sendMessage(
                    languageConfig
                            .getMessage("command.to.error.notSetUser")
                            .toString()
            );
        } else {
            Player player = (Player) sender;
            Player mentionedPlayer = Bukkit.getPlayer(args[1]);
            if (mentionedPlayer == null) {
                languageConfig.getMessage("command.to.error.undefinedUser").setCustomMessage(1, args[1]).sendMessage(player);
            } else if (player.getName().equals(mentionedPlayer.getName())) {
                languageConfig.getMessage("command.to.error.onHimselfItself").sendMessage(player);
            } else {
                this.initSession(player, mentionedPlayer);
            }
        }

        return true;
    }

    /**
     * Init session between players
     *
     * @param seller Seller trade
     * @param buyer  Buyer trade
     * @return Status command
     */
    private void initSession(Player seller, Player buyer) {
        try {
            //If session already exits, just open inventory on seller
            TradeSession session = this.tradeSessionManager.getSessionForSellerOrBuyer(seller, buyer);
            seller.openInventory(session.getInventory());
        } catch (TradeSessionManagerException e) {
            if (!buyer.isOnline()) {
                this.languageConfig.getMessage("trade.player.offline").setBuyer(buyer).sendMessage(buyer);
            } else {
                this.languageConfig.getMessage("command.to.message").setBuyer(buyer).sendMessage(seller);
                this.languageConfig.getMessage("command.confirm.message").setBuyer(buyer).setSeller(seller).sendMessage(buyer);
                this.tradeSessionManager.createSession(seller, buyer);
            }
        }
    }


    @Override
    public boolean isCanBeProcessed(CommandSender sender, String[] args) {
        return sender instanceof Player && args.length != 0 && args[0].equalsIgnoreCase("to");
    }
}
