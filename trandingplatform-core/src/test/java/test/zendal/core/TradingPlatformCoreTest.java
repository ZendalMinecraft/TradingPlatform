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
import ru.zendal.core.config.TradingPlatformConfiguration;
import ru.zendal.core.config.server.TypeSource;
import test.zendal.core.economy.EconomyProviderTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TradingPlatformCoreTest {

    private final static String CONFIG_PATH = "var/TradingPlatform/";

    private TradingPlatformCore core;

    @BeforeEach
    public void setup() {
       // this.core = TradingPlatformCore.builder().pathToRoot(CONFIG_PATH).economyProvider(new EconomyProviderTest()).build();
    }

    @AfterEach
    public void clear() {
      //  new File(CONFIG_PATH + "core.json").delete();
    }

    //TODO remove...
    public void createNewDefaultConfigurationFile() {
        TradingPlatformConfiguration configuration = this.core.getConfiguration();
        assertEquals((Long) 1L, configuration.getVersion());
        assertEquals(TypeSource.LOCAL, configuration.getServerSideConfiguration().getDataSource().getTypeSource());
        assertFalse(configuration.getServerSideConfiguration().getSocket().isEnable());
    }


}
