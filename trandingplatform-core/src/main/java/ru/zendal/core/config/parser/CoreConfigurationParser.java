package ru.zendal.core.config.parser;

import ru.zendal.core.config.TradingPlatformConfiguration;
import ru.zendal.core.config.parser.engine.JsonEngineParser;
import ru.zendal.core.config.parser.exception.TradingPlatformConfigurationParserException;
import ru.zendal.core.config.server.DataBaseServerConfiguration;
import ru.zendal.core.config.server.SocketServerConfiguration;
import ru.zendal.core.config.server.TypeSource;

import java.io.File;

/**
 * Parser for get configuration for core (THIS) element
 */
public class CoreConfigurationParser extends TradingPlatformConfigurationParser<TradingPlatformConfiguration> {

    /**
     * Instance of wrapper file, store data about configuration file.
     */
    private final File configurationFile;

    /**
     * Object of configuration.
     * Support lazy load
     */
    private TradingPlatformConfiguration tradingPlatformConfiguration;

    /**
     * Instantiates a new Core configuration parser.
     *
     * @param jsonEngineParser the json engine parser
     * @param rootPath         the root path
     */
    public CoreConfigurationParser(JsonEngineParser jsonEngineParser, String rootPath) {
        super(jsonEngineParser);
        configurationFile = new File(rootPath + "/" + this.getNameConfigurationFile());
    }

    @Override
    public TradingPlatformConfiguration getConfiguration() throws TradingPlatformConfigurationParserException {
        if (this.tradingPlatformConfiguration == null) {
            this.tradingPlatformConfiguration = this.loadConfiguration(configurationFile, this.initializeNewConfigurationFile());
        }
        return this.tradingPlatformConfiguration;
    }

    /**
     * Save configuration into file.
     *
     * @param configuration instance object of configuration
     */
    public void save(TradingPlatformConfiguration configuration) {
        this.tradingPlatformConfiguration = this.saveConfiguration(configurationFile, configuration);
    }


    @Override
    public String getNameConfigurationFile() {
        return "core.json";
    }

    /**
     * Create default Trading platform configuration structure
     *
     * @return Trading platform configuration object
     */
    private TradingPlatformConfiguration initializeNewConfigurationFile() {
        return TradingPlatformConfiguration.builder()
                .version(1L)
                .serverSideConfiguration(TradingPlatformConfiguration.ServerSideConfiguration.builder()
                        .dataSource(
                                DataBaseServerConfiguration.builder()
                                        .typeSource(TypeSource.LOCAL)
                                        .host("127.0.0.1")
                                        .nameDataBase("TradingPlatform")
                                        .password("")
                                        .port(3306)
                                        .username("root")
                                        .build()
                        )
                        .socket(
                                SocketServerConfiguration.builder()
                                        .enable(false)
                                        .host("127.0.0.1")
                                        .port(2101)
                                        .build())
                        .build()
                ).build();
    }
}
