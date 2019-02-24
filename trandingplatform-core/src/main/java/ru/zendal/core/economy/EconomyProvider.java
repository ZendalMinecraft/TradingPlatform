/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.zendal.core.core.economy;

import test.zendal.core.core.economy.exception.EconomyProviderException;

public interface EconomyProvider<P> {
    /**
     * Check Player balance
     *
     * @param player Instance offline Player
     * @param money  Need money
     * @return {@code true} if player has "need money"
     */
    boolean haveMoney(P player, double money);

    /**
     * Withdraw (-) money
     *
     * @param player Instance offline Player
     * @param amount Amount money
     * @throws EconomyProviderException on Transaction error
     */
    void withdraw(P player, double amount) throws EconomyProviderException;

    /**
     * Can withdraw (-) money
     *
     * @param player Instance offline Player
     * @param amount AMount money
     * @return {@code true} if can else {@code false}
     */
    boolean canWithdraw(P player, double amount);


    /**
     * Deposit (+) money
     *
     * @param player Instance offline Player
     * @param amount Amount money
     * @throws EconomyProviderException on Transaction error
     */
    void deposit(P player, double amount) throws EconomyProviderException;


    /**
     * Get balance player
     *
     * @param player Player
     * @return Balance amount
     */
    double getBalance(P player);
}
