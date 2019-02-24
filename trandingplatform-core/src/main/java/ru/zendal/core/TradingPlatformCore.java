/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.core;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zendal.core.config.TradingPlatformConfiguration;
import ru.zendal.core.config.parser.CoreConfigurationParser;
import ru.zendal.core.config.parser.engine.GsonParser;
import ru.zendal.core.economy.EconomyProvider;
import ru.zendal.core.exception.TradingPlatformLaunchException;

@Data
@Builder(buildMethodName = "buildInternal")
@AllArgsConstructor
public class TradingPlatformCore {

    private final Logger logger = LoggerFactory.getLogger(TradingPlatformCore.class);

    private final String pathToRoot;


    private final EconomyProvider economyProvider;

    private CoreConfigurationParser parser;

    private void init() {
        try {
            logger.info("Initialize core TradingPlatformCore.");
            parser = new CoreConfigurationParser(new GsonParser(), pathToRoot);
        } catch (Exception e) {
            logger.error("ERROR launch core!", e);
            throw new TradingPlatformLaunchException("Error launch core.", e);
        }
    }

    /**
     * Return configuration
     *
     * @return trading platform configuration object
     */
    public TradingPlatformConfiguration getConfiguration() {
        return parser.getConfiguration();
    }


    public static class TradingPlatformCoreBuilder {

        public TradingPlatformCore build() {
            TradingPlatformCore core = this.buildInternal();
            core.init();
            return core;
        }

    }


}
