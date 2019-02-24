/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.zendal.core.economy;

import ru.zendal.core.economy.EconomyProvider;
import ru.zendal.core.economy.exception.EconomyProviderException;

import java.util.HashMap;
import java.util.Map;

public class EconomyProviderTest implements EconomyProvider<String> {


    private Map<String, Double> storage = new HashMap<>();

    @Override
    public boolean haveMoney(String player, double money) {
        if (storage.containsKey(player)) {
            return storage.get(player) - money >= 0;
        }
        return false;
    }

    @Override
    public void withdraw(String player, double amount) throws EconomyProviderException {
        Double money = storage.get(player);
        if (money == null) {
            throw new EconomyProviderException("Money 0");
        } else if (money - amount < 0) {
            throw new EconomyProviderException("Money 0");
        }
        storage.remove(player);
        storage.put(player, money - amount);
    }

    @Override
    public boolean canWithdraw(String player, double amount) {
        Double money = storage.get(player);
        if (money == null && amount != 0) {
            return false;
        }
        money = 0.0;
        if (money - amount < 0) {
            return false;
        }
        return true;
    }

    @Override
    public void deposit(String player, double amount) throws EconomyProviderException {
        Double money = storage.get(player);
        storage.remove(player);
        storage.put(player, money + amount);
    }

    @Override
    public double getBalance(String player) {
        Double money = storage.get(player);
        return money == null ? 0.0 : money;
    }
}
