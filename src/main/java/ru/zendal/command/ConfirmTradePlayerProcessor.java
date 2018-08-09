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

import java.util.List;

public class ConfirmTradePlayerProcessor implements ArgsCommandProcessor {

    private final TradeSessionManager tradeSessionManager;
    private final LanguageConfig languageConfig;

    public ConfirmTradePlayerProcessor(TradeSessionManager sessionManager, LanguageConfig languageConfig) {
        this.tradeSessionManager = sessionManager;
        this.languageConfig = languageConfig;
    }

    @Override
    public boolean process(Command command, CommandSender sender, String[] args) {
        Player buyer = (Player) sender;
        int countSessions = this.tradeSessionManager.getCountSessionsBuyer(buyer);
        if (countSessions == 0) {
            this.languageConfig.getMessage("trade.confirm.notHaveSession").sendMessage(buyer);
            return true;
        } else {
            return this.confirmSession(countSessions, buyer, args);
        }
    }

    /**
     * Confirm session if countSession >=0
     *
     * @param countSessions Count sessions for buyer
     * @param buyer         Buyer
     * @param args          arguments command
     * @return Status command
     */
    private boolean confirmSession(int countSessions, Player buyer, String[] args) {
        if (args.length > 1) {
            Player seller = Bukkit.getPlayer(args[1]);
            if (seller == null) {
                languageConfig.getMessage("command.to.error.undefinedUser").setCustomMessage(1, args[1]).sendMessage(buyer);
                return true;
            }
            try {
                TradeSession session = this.tradeSessionManager.getSessionForSellerAndBuyer(seller, buyer);
                this.openTradeSessionAndNotificateSeller(session);
                return true;
            } catch (TradeSessionManagerException e) {
                this.languageConfig.getMessage("trade.confirm.select.undefinedSession").setBuyer(buyer).setSeller(seller).sendMessage(buyer);
                return true;
            }
        }
        if (countSessions == 1) {
            try {
                TradeSession session = this.tradeSessionManager.getSessionByBuyer(buyer);
                this.openTradeSessionAndNotificateSeller(session);
                return true;
            } catch (TradeSessionManagerException e) {
                e.printStackTrace();
                return false;
            }
        } else if (countSessions > 1) {
            //If Buyer have many session and not type buyer
            this.languageConfig.getMessage("trade.confirm.list").
                    setCustomMessage(
                            1,
                            this.prepareListTradeSessionToListSeller(
                                    this.tradeSessionManager.getAllAvailableSessionsForBuyer(buyer)
                            )
                    ).sendMessage(buyer);
            return true;
        }
        return false;
    }


    private void openTradeSessionAndNotificateSeller(TradeSession session) {
        session.getBuyer().openInventory(session.getInventory());
        this.languageConfig.getMessage("command.to.message.onSecondPlayerConfirm").
                setBuyer(session.getBuyer()).
                sendMessage(session.getSeller());

    }

    /**
     * Prepare list Trade Sessions to List DisplayName Sellers
     *
     * @param sessionList Trade sessions
     * @return sellers string
     */
    private String prepareListTradeSessionToListSeller(List<TradeSession> sessionList) {
        StringBuilder builder = new StringBuilder();
        for (TradeSession session : sessionList) {
            builder.append(session.getSeller().getDisplayName()).append(", ");
        }
        return builder.deleteCharAt(builder.length() - 1).deleteCharAt(builder.length() - 1).toString();
    }

    @Override
    public boolean isCanBeProcessed(CommandSender sender, String[] args) {
        return sender instanceof Player && args.length != 0 && args[0].equalsIgnoreCase("confirm");
    }
}
