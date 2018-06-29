package ru.zendal.session.economy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class VaultEconomy implements Economy {


    private final net.milkbowl.vault.economy.Economy economy;

    private final Logger logger;

    public VaultEconomy(JavaPlugin plugin) {
        logger = plugin.getLogger();
        RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        } else {
            logger.warning("Vault plugin not connected, some functions can be not working");
            economy = null;
        }
    }


    @Override
    public boolean hasMoney(Player player, double money) {
        if (!this.isEnable()) {
            return false;
        }
        return (economy.getBalance(player) - money) >= 0;
    }


    @Override
    public boolean isEnable() {
        return economy != null;
    }
}
