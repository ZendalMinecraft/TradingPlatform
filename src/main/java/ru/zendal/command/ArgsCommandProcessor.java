package ru.zendal.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ArgsCommandProcessor {


    boolean process(Command command, CommandSender sender, String args[]);

    boolean isCanBeProcessed(CommandSender sender, String[] args);
}
