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
 * Class for process:
 * Create trade between players
 */
public class TradeBetweenPlayer implements ArgsCommandProcessor {


    private final TradeSessionManager tradeSessionManager;
    private final LanguageConfig languageConfig;

    public TradeBetweenPlayer(TradeSessionManager sessionManager, LanguageConfig languageConfig) {
        this.tradeSessionManager = sessionManager;
        this.languageConfig = languageConfig;
    }

    @Override
    public boolean process(Command command, CommandSender sender, String[] args) {
        Player seller = (Player) sender;
        if (args.length < 2) {
            languageConfig.getMessage("trade.create.between.notSetUser").sendMessage(seller);
            return true;
        }

        Player buyer = Bukkit.getPlayer(args[1]);

        if (buyer == null) {
            languageConfig.getMessage("trade.create.between.undefinedUser").setCustomMessage(1, args[1]).sendMessage(seller);
            return true;
        }


        return false;
    }

    private boolean initSession(Player seller,Player buyer){

        try {
            //If session already exits, just open inventory on seller
            TradeSession session =  this.tradeSessionManager.getSessionForSellerAndBuyer(seller,buyer);
            seller.openInventory(session.getInventory());
        } catch (TradeSessionManagerException e) {
           //Else create session

        }

    }


    @Override
    public boolean isCanBeProcessed(CommandSender sender, String[] args) {
        return sender instanceof Player && args.length != 0 && args[0].equalsIgnoreCase("to");
    }
}
