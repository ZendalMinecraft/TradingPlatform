package ru.zendal.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.zendal.session.TradeSession;
import ru.zendal.TradePlace;
import ru.zendal.session.exception.TradeSessionManagerException;


public class CommandProcessor implements CommandExecutor {


    private final TradePlace plugin;

    public CommandProcessor(TradePlace instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        if (args[0].equalsIgnoreCase("confirm")) {
            try {
                TradeSession session = this.plugin.getSessionManager().getSessionByBuyer((Player) sender);
                Bukkit.broadcastMessage(session.getInventory().toString());
                ((Player) sender).openInventory(session.getInventory());
                session.getSeller().openInventory(session.getInventory());
                return true;
            } catch (TradeSessionManagerException e) {
                e.printStackTrace();
            }
        }

        this.plugin.getSessionManager().createSession((Player) sender, Bukkit.getPlayer("gasfull"));
        // ((Player) sender).openInventory(inventory);
        Player player = Bukkit.getPlayer("gasfull");
        Bukkit.getPlayer("gasfull").sendMessage("Confirm trade");

        return true;
    }
}
