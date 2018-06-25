package ru.zendal.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.exception.TradeSessionManagerException;

public class GetStorage implements ArgsCommandProcessor {


    private final TradeSessionManager manager;
    private final LanguageConfig languageConfig;

    public GetStorage(TradeSessionManager manager, LanguageConfig languageConfig){
        this.manager = manager;
        this.languageConfig = languageConfig;
    }

    @Override
    public boolean process(Command command, CommandSender sender, String[] args) {
        try {
            Inventory inventory =  this.manager.getInventorySabotageForPlayer((Player) sender);
            ((Player) sender).openInventory(inventory);
        } catch (TradeSessionManagerException e) {
            this.languageConfig.getMessage("trade.storage.clear").sendMessage((Player) sender);
        }
        return true;
    }

    @Override
    public boolean isCanBeProcessed(CommandSender sender, String[] args) {
        return args.length>0 && args[0].equalsIgnoreCase("storage");
    }
}
