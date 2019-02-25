/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.zendal.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.zendal.core.TradingPlatformCore;
import ru.zendal.core.economy.EconomyProvider;
import ru.zendal.core.economy.exception.EconomyProviderException;
import ru.zendal.core.session.TradeSessionManager;
import ru.zendal.core.session.modal.DefaultTradeSession;
import test.zendal.core.economy.EconomyProviderTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static test.zendal.core.TestConfig.PATH_TO_FOLDER_TEST;

public class TradingPlatformCoreTest {

    private final String CONFIG_PATH = PATH_TO_FOLDER_TEST + "/TradingPlatform/" + getClass().toString();

    private TradeSessionManager<String, String> tradeSessionManager;

    private EconomyProvider<String> economyProvider;

    @BeforeEach
    void setup() {
        economyProvider = new EconomyProviderTest();
        this.tradeSessionManager = TradingPlatformCore.builder().pathToRoot(CONFIG_PATH).economyProvider(economyProvider).build().getTradeSessionManager();
    }

    @AfterEach
    void clear() {
        new File(CONFIG_PATH + "core.json").delete();
    }

    @Test
    void testCreateTradeManager() throws EconomyProviderException {
        economyProvider.deposit("A", 20.0);
        economyProvider.deposit("B", 20.0);

        DefaultTradeSession<String, String> tradeSession = tradeSessionManager.createTradeSession("A", "B");
        tradeSession.setBetMainTrader(15.2);
        tradeSession.setBetSecondaryTrader(10.0);
        tradeSessionManager.confirmTradeSession(tradeSession);

        assertEquals(14.8, economyProvider.getBalance("A"));
        assertEquals(25.2, economyProvider.getBalance("B"));
    }


}
