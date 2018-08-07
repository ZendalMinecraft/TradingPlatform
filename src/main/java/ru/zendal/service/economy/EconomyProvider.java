/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.service.economy;

import org.bukkit.OfflinePlayer;
import ru.zendal.service.economy.exception.EconomyProviderException;

public interface EconomyProvider {

    /**
     * Check Player balance
     *
     * @param player Instance offline Player
     * @param money  Need money
     * @return {@code true} if player has "need money"
     */
    boolean haveMoney(OfflinePlayer player, double money);

    /**
     * Withdraw (-) money
     *
     * @param player Instance offline Player
     * @param amount Amount money
     * @throws EconomyProviderException on Transaction error
     */
    void withdraw(OfflinePlayer player, double amount) throws EconomyProviderException;


    /**
     * Deposit (+) money
     *
     * @param player Instance offline Player
     * @param amount Amount money
     * @throws EconomyProviderException on Transaction error
     */
    void deposit(OfflinePlayer player, double amount) throws EconomyProviderException;


    /**
     * Get balance player
     *
     * @param player Player
     * @return Balance amount
     */
    double getBalance(OfflinePlayer player);
}
