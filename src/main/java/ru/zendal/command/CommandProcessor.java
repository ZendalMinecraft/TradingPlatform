/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.zendal.TradingPlatform;

import java.util.ArrayList;
import java.util.List;

/**
 * Command processor
 */
public class CommandProcessor implements CommandExecutor {


    private final TradingPlatform plugin;
    /**
     * List processors command
     */
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
