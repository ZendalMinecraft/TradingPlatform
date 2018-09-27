/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.service.economy;

import org.bukkit.OfflinePlayer;
import ru.zendal.service.economy.exception.EconomyProviderException;

public class DisabledEconomy implements EconomyProvider {
    @Override
    public boolean haveMoney(OfflinePlayer player, double money) {
        return true;
    }

    @Override
    public void withdraw(OfflinePlayer player, double amount) throws EconomyProviderException {

    }

    @Override
    public boolean canWithdraw(OfflinePlayer player, double amount) {
        return true;
    }

    @Override
    public void deposit(OfflinePlayer player, double amount) throws EconomyProviderException {

    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return 0;
    }
}
