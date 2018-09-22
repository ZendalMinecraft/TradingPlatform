/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.service.economy;

import com.google.inject.Inject;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import ru.zendal.service.economy.exception.EconomyProviderException;

/**
 * Vault EconomyProvider
 *
 * @link https://github.com/MilkBowl/Vault
 */
public class VaultEconomy implements EconomyProvider {

    /**
     * Instance Vault economy.
     */
    private final Economy economy;

    /**
     * Constructor.
     *
     * @param pluginManager   Plugin Manager
     * @param servicesManager Service Manager
     */
    @Inject
    public VaultEconomy(PluginManager pluginManager, ServicesManager servicesManager) {
        if (pluginManager.getPlugin("Vault") == null) {
            throw new EconomyProviderException("Required plugin Vault don't founded");
        }

        RegisteredServiceProvider<Economy> registeredServiceProvider = servicesManager.getRegistration(Economy.class);
        if (registeredServiceProvider == null) {
            throw new EconomyProviderException("Can't get Vault service");
        }
        economy = registeredServiceProvider.getProvider();
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
    public boolean canWithdraw(OfflinePlayer player, double amount) {
        EconomyResponse economyResponse = economy.withdrawPlayer(player, amount);
        if (economyResponse.transactionSuccess()) {
            economy.depositPlayer(player, amount);
            return true;
        }
        return false;
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
