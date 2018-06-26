package ru.zendal.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.TradeSessionManager;

public class TradeCreate implements ArgsCommandProcessor{


    private final TradeSessionManager manager;

    public TradeCreate(TradeSessionManager manager, LanguageConfig languageConfig){
        this.manager = manager;
    }

    @Override
    public boolean process(Command command, CommandSender sender, String[] args) {
        manager.createSession((Player) sender);
        return true;
    }

    @Override
    public boolean isCanBeProcessed(CommandSender sender, String[] args) {
        return args.length>=1 && args[0].equalsIgnoreCase("create");
    }
}
