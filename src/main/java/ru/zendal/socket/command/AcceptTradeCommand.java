/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.socket.command;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.zendal.service.economy.EconomyProvider;
import ru.zendal.session.TradeOffline;
import ru.zendal.session.TradeOfflineConfirmResponse;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.exception.TradeSessionManagerException;
import ru.zendal.socket.command.exception.ProcessCommandException;

import java.util.UUID;

public class AcceptTradeCommand implements Command {
    private final TradeSessionManager sessionManager;
    private final EconomyProvider economyProvider;

    public AcceptTradeCommand(TradeSessionManager sessionManager, EconomyProvider economyProvider) {
        this.sessionManager = sessionManager;
        this.economyProvider = economyProvider;
    }

    @Override
    public Document process(Document incomingDocument) throws ProcessCommandException {
        Document document = (Document) incomingDocument.get("data");
        if (document == null) {
            throw new ProcessCommandException();
        }

        String uuidPlayer = document.getString("uuidPlayer");

        String idTrade = document.getString("idTrade");

        try {
            TradeOffline tradeOffline = sessionManager.getTradeOfflineById(idTrade);

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuidPlayer));

            if (offlinePlayer == null) {
                throw new ProcessCommandException();
            }
            if (!offlinePlayer.isOnline()) {
                throw new ProcessCommandException();
            }
            Player player = offlinePlayer.getPlayer();

            TradeOfflineConfirmResponse response = tradeOffline.confirmTrade(player,economyProvider);
            if (response.hasMissingItems()) {
                throw new ProcessCommandException();
            }


            //TODO Move to SessionManager
            try {
                sessionManager.removeTradeOffline(tradeOffline);
                player.getInventory().clear();
                player.getInventory().setContents(response.getNewContent());
                sessionManager.processTradeOffline(player, tradeOffline);
            } catch (TradeSessionManagerException e) {
                // languageConfig.getMessage("trade.offline.alreadyFinished").sendMessage(player);
            }

            /*offline.getOfflinePlayer().get*/
        } catch (TradeSessionManagerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getNameCommand() {
        return "acceptTrade";
    }
}
