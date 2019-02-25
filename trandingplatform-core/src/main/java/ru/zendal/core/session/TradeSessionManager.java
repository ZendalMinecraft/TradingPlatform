package ru.zendal.core.session;

import ru.zendal.core.config.TradingPlatformConfiguration;
import ru.zendal.core.economy.EconomyProvider;
import ru.zendal.core.economy.exception.EconomyProviderException;
import ru.zendal.core.session.exception.EconomyTradeSessionException;
import ru.zendal.core.session.modal.DefaultTradeSession;

public final class TradeSessionManager<T, I> {

    private final TradingPlatformConfiguration tradingPlatformConfiguration;

    private final EconomyProvider economyProvider;


    public TradeSessionManager(TradingPlatformConfiguration configuration, EconomyProvider economyProvider) {
        this.tradingPlatformConfiguration = configuration;
        this.economyProvider = economyProvider;
    }

    public DefaultTradeSession<T, I> createTradeSession(T mainTrader, T secondaryTrader) {
        return new DefaultTradeSession<>(mainTrader, secondaryTrader);
    }

    public void confirmTradeSession(DefaultTradeSession<T, I> tradeSession) {
        boolean result = economyProvider.canWithdraw(tradeSession.getMainTrader(), tradeSession.getBetMainTrader());
        result = result && economyProvider.canWithdraw(tradeSession.getSecondaryTrader(), tradeSession.getBetSecondaryTrader());
        if (!result) {
            throw new EconomyTradeSessionException("User not have money");
        }
        try {
            economyProvider.withdraw(tradeSession.getMainTrader(), tradeSession.getBetMainTrader());
            economyProvider.withdraw(tradeSession.getSecondaryTrader(), tradeSession.getBetSecondaryTrader());

            economyProvider.deposit(tradeSession.getMainTrader(), tradeSession.getBetSecondaryTrader());
            economyProvider.deposit(tradeSession.getSecondaryTrader(), tradeSession.getBetMainTrader());
        } catch (EconomyProviderException e) {
            throw new EconomyTradeSessionException("User not have money");
        }
    }
}
