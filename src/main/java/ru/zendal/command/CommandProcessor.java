package ru.zendal.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.session.inventory.CreateTradeHolderInventory;
import ru.zendal.TradingPlatform;
import ru.zendal.config.AdaptiveMessage;
import ru.zendal.session.TradeSession;
import ru.zendal.session.exception.TradeSessionManagerException;

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
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        for (ArgsCommandProcessor processor : processors) {
            if (processor.isCanBeProcessed(sender, args)) {
                return processor.process(command, sender, args);
            }
        }


        return false;
/*
        if (args.length == 0) {
            return this.processCommandTrade(player);
        } else if (args[0].equalsIgnoreCase("confirm")) {
            try {
                return this.processConfirmTrade(player, args);
            } catch (TradeSessionManagerException e) {
                this.plugin.getLogger().warning("Have some error with confirm trade:");
                e.printStackTrace();
            }
        } else if (args[0].equalsIgnoreCase("create")) {
            return this.createTrade(player);
        }else if (args[0].equalsIgnoreCase("to")){
            return  this.createTradeBetweenPlayer(player);
        }

        this.plugin.getSessionManager().createSession((Player) sender, Bukkit.getPlayer("gasfull"));
        Bukkit.getPlayer("gasfull").sendMessage(this.getAdaptiveMessageByMessage("trade.confirm").setBuyer(player).setSeller(((Player) sender).getPlayer()).toString());

        return true;*/
    }


    private boolean processCommandTrade(Player player) {
        this.getAdaptiveMessageByMessage("trade.help.message").sendMessage(player);
        return true;
    }

    /**
     * Process confirm trade by buyer
     *
     * @param player Buyer
     * @param args   arguments command
     * @return Status code {@code true} if success else {@code false}
     * @throws TradeSessionManagerException At problems with sessions
     */
    private boolean processConfirmTrade(Player player, String[] args) throws TradeSessionManagerException {
        int countSessions = this.plugin.getSessionManager().getCountSessionsBuyer(player);
        if (countSessions == 0) {
            player.sendMessage(
                    this.getAdaptiveMessageByMessage("trade.confirm.notHaveSession").toString()
            );
            return true;
        } else if (countSessions == 1) {
            TradeSession session = this.plugin.getSessionManager().getSessionByBuyer(player);
            player.openInventory(session.getInventory());
            session.getSeller().openInventory(session.getInventory());
            return true;
        } else if (args.length >= 2) {
            Player seller = Bukkit.getPlayer(args[1]);
            try {
                TradeSession session = this.plugin.getSessionManager().getSessionForSellerAndBuyer(seller, player);
                player.openInventory(session.getInventory());
                session.getSeller().openInventory(session.getInventory());
            } catch (TradeSessionManagerException e) {
                player.sendMessage(
                        this.getAdaptiveMessageByMessage(e.getMessage()).setSeller(seller).setBuyer(player).toString()
                );
            }
            return true;
        } else {

        }
        return false;
    }


    private boolean createTrade(Player player) {
        Inventory inventory = Bukkit.createInventory(new CreateTradeHolderInventory(), 27, "Create trade");
        //TODO Create red wool and green
        inventory.setItem(26, new ItemStack(Material.WOOL));
        player.openInventory(inventory);
        return true;
    }


    private boolean createTradeBetweenPlayer(Player player, String[] args) {
        if (args.length < 2) {
            this.getAdaptiveMessageByMessage("trade.create.between.notSetUser").sendMessage(player);
            return true;
        }

        Player buyer = Bukkit.getPlayer(args[1]);
        if (buyer == null) {
            this.getAdaptiveMessageByMessage("trade.create.between.undefinedUser").setCustomMessage(1, args[1]).sendMessage(player);
            return true;
        }
        return true;
    }

    private AdaptiveMessage getAdaptiveMessageByMessage(String message) {
        return this.plugin.getTradingPlatformConfig().getLanguageConfig().getMessage(message);
    }
}
