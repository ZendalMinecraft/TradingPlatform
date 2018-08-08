/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.service.economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
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
    public boolean haveMoney(OfflinePlayer player, double money) {
        return economy.has(player, money);
    }

    @Override
    public void withdraw(OfflinePlayer player, double amount) throws EconomyProviderException {
        EconomyResponse economyResponse = economy.withdrawPlayer(player, amount);
        if (!economyResponse.transactionSuccess()) {
            throw new EconomyProviderException("Transaction withdraw error: " + economyResponse.errorMessage);
        }
    }

    @Override
    public void deposit(OfflinePlayer player, double amount) throws EconomyProviderException {
        EconomyResponse economyResponse = economy.depositPlayer(player, amount);
        if (!economyResponse.transactionSuccess()) {
            throw new EconomyProviderException("Transaction deposit error: " + economyResponse.errorMessage);
        }
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return economy.getBalance(player);
    }

}
