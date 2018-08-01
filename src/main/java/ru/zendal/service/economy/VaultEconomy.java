/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.service.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import ru.zendal.service.economy.exception.EconomyProviderException;

public class VaultEconomy implements EconomyProvider {

    private final Economy economy;

    public VaultEconomy(Server server) {
        if (server.getPluginManager().getPlugin("Vault") == null) {
            throw new EconomyProviderException("Required plugin Vault don't founded");
        }

        RegisteredServiceProvider<Economy> rsp = server.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            throw new EconomyProviderException("Can't get Vault service");
        }
        economy = rsp.getProvider();
        if (economy == null) {
            throw new EconomyProviderException("Can't get Vault provider");
        }
    }

    @Override
    public boolean haveMoney(Player player, double money) {
        economy.depositPlayer()
        return economy.has(player, money);
    }

    pub
}
