package ru.zendal.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.zendal.TradingPlatform;

import java.util.ArrayList;
import java.util.List;


public class CommandProcessor implements CommandExecutor {


    private final TradingPlatform plugin;

    private List<ArgsCommandProcessor> processors = new ArrayList<>();


    public CommandProcessor(TradingPlatform instance) {
        this.plugin = instance;
        this.initArgsProcessors();
    }

    private void initArgsProcessors() {
        processors.add(new TradeBetweenPlayer(
                this.plugin.getSessionManager(),
                this.plugin.getTradingPlatformConfig().getLanguageConfig()
        ));

        processors.add(new TradeConfirmBetweenPlayer(
                this.plugin.getSessionManager(),
                this.plugin.getTradingPlatformConfig().getLanguageConfig()
        ));

        processors.add(new GetStorage(
                this.plugin.getSessionManager(),
                this.plugin.getTradingPlatformConfig().getLanguageConfig()
        ));

        processors.add(new TradeCreate(
                this.plugin.getSessionManager(),
                this.plugin.getTradingPlatformConfig().getLanguageConfig()
        ));

        processors.add(new OpenOfflineSessionProcessor(
                this.plugin.getSessionManager(),
                this.plugin.getTradingPlatformConfig().getLanguageConfig()
        ));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for (ArgsCommandProcessor processor : processors) {
            if (processor.isCanBeProcessed(sender, args)) {
                return processor.process(command, sender, args);
            }
        }

        return false;
    }
}
