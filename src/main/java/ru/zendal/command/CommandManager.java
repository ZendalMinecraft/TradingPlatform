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
import org.bukkit.entity.Player;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.TradeSessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Command manager
 * Register all Command Processor
 */
public class CommandManager implements CommandExecutor {

    /**
     * Instance TradeSessionManager
     */
    private final TradeSessionManager sessionManager;
    /**
     * Instance language config
     */
    private final LanguageConfig languageConfig;
    /**
     * List processors command
     */
    private List<ArgsCommandProcessor> processors = new ArrayList<>();


    /**
     * Constructor
     *
     * @param sessionManager Instance TradeSessionManager
     * @param languageConfig Instance language config
     */
    public CommandManager(TradeSessionManager sessionManager, LanguageConfig languageConfig) {
        this.sessionManager = sessionManager;
        this.languageConfig = languageConfig;
        this.initArgsProcessors();
    }

    /**
     * Init Command processors
     *
     * @see TradeBetweenPlayer
     * @see TradeConfirmBetweenPlayer
     * @see StorageCommandProcessor
     * @see TradeCreate
     * @see OpenOfflineSessionProcessor
     */
    private void initArgsProcessors() {
        processors.add(new TradeBetweenPlayer(sessionManager, languageConfig));

        processors.add(new TradeConfirmBetweenPlayer(sessionManager, languageConfig));

        processors.add(new StorageCommandProcessor(sessionManager, languageConfig));

        processors.add(new TradeCreate(sessionManager, languageConfig));

        processors.add(new OpenOfflineSessionProcessor(sessionManager, languageConfig));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for (ArgsCommandProcessor processor : processors) {
            if (processor.isCanBeProcessed(sender, args)) {
                return processor.process(command, sender, args);
            }
        }
        languageConfig.getMessage("command.help.message").sendMessage((Player) sender);
        return true;
    }
}
