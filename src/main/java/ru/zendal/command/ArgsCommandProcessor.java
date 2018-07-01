/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Interface command processor
 */
public interface ArgsCommandProcessor {

    /**
     * Process command
     *
     * @param command Represents a Command, which executes various tasks upon user input
     * @param sender  Sender command
     * @param args    Arguments command
     * @return {@code true} if process command end with success status else {@code false}
     * @see Command
     * @see CommandSender
     */
    boolean process(Command command, CommandSender sender, String args[]);

    /**
     * Can be this processor process command
     *
     * @param sender Sender command
     * @param args   Arguments command
     * @return {@code true} if can be process else {@code false}
     * @see CommandSender
     */
    boolean isCanBeProcessed(CommandSender sender, String[] args);
}
