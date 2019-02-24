package test.zendal.core.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.zendal.core.config.TradingPlatformConfiguration;
import ru.zendal.core.config.parser.CoreConfigurationParser;
import ru.zendal.core.config.parser.engine.GsonParser;
import ru.zendal.core.config.server.DataBaseServerConfiguration;
import ru.zendal.core.config.server.TypeSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static test.zendal.core.TestConfig.PATH_TO_FOLDER_TEST;

/**
 * Tests for core configuration parser
 */
class CoreConfigurationTest {

    /**
     * Instance of configuration parser
     */
    private CoreConfigurationParser parser;

    /**
     * Path for storage test file
     */
    private final static String CONFIG_PATH = PATH_TO_FOLDER_TEST + "/TradingPlatform/".getClass().toString();

    /**
     * Setup method.
     */
    @BeforeEach
    void setup() {
        parser = new CoreConfigurationParser(new GsonParser(), CONFIG_PATH);
    }

    /**
     * Finish method.
     */
    @AfterEach
    void end() {
        new File(CONFIG_PATH + "/" + this.parser.getNameConfigurationFile()).delete();
    }

    /**
     * Test init configuration file.
     */
    @Test
    void testInitializeConfigurationFile() {
        this.assertAllPropertyConfiguration(parser.getConfiguration());
    }

    /**
     * Test save configuration file.
     */
    @Test
    void testSaveConfigurationFile() {
        TradingPlatformConfiguration configuration = parser.getConfiguration();
        configuration.setVersion(2L);
        DataBaseServerConfiguration dataBaseServerConfiguration = configuration.getServerSideConfiguration().getDataSource();
        dataBaseServerConfiguration.setPassword("SECRET");
        dataBaseServerConfiguration.setTypeSource(TypeSource.MONGO_DB);
        TradingPlatformConfiguration.ServerSideConfiguration serverSideConfiguration = configuration.getServerSideConfiguration();
        serverSideConfiguration.setDataSource(dataBaseServerConfiguration);
        configuration.setServerSideConfiguration(serverSideConfiguration);

        parser.save(configuration);

        configuration = parser.getConfiguration();

        assertEquals((Long) 2L, configuration.getVersion());
        /* Check data source block */
        assertEquals(TypeSource.MONGO_DB, configuration.getServerSideConfiguration().getDataSource().getTypeSource());
        assertEquals("127.0.0.1", configuration.getServerSideConfiguration().getDataSource().getHost());
        assertEquals("TradingPlatform", configuration.getServerSideConfiguration().getDataSource().getNameDataBase());
        assertEquals("root", configuration.getServerSideConfiguration().getDataSource().getUsername());
        assertEquals("SECRET", configuration.getServerSideConfiguration().getDataSource().getPassword());
        assertEquals(3306, configuration.getServerSideConfiguration().getDataSource().getPort());

        /* Check socket block */
        assertFalse(configuration.getServerSideConfiguration().getSocket().isEnable());
        assertEquals("127.0.0.1", configuration.getServerSideConfiguration().getSocket().getHost());
        assertEquals((Integer) 2101, configuration.getServerSideConfiguration().getSocket().getPort());
    }

    /**
     * Test load configuration file, if file already exists
     */
    @Test
    void testLoadConfigurationFileFromFolder() {
        parser.getConfiguration();
        parser = new CoreConfigurationParser(new GsonParser(), CONFIG_PATH);
        this.assertAllPropertyConfiguration(parser.getConfiguration());
    }

    /**
     * Assert all property in configuration object. Compare with default value
     *
     * @param configuration instance of configuration
     */
    private void assertAllPropertyConfiguration(TradingPlatformConfiguration configuration) {
        assertEquals((Long) 1L, configuration.getVersion());
        /* Check data source block */
        assertEquals(TypeSource.LOCAL, configuration.getServerSideConfiguration().getDataSource().getTypeSource());
        assertEquals("127.0.0.1", configuration.getServerSideConfiguration().getDataSource().getHost());
        assertEquals("TradingPlatform", configuration.getServerSideConfiguration().getDataSource().getNameDataBase());
        assertEquals("root", configuration.getServerSideConfiguration().getDataSource().getUsername());
        assertEquals("", configuration.getServerSideConfiguration().getDataSource().getPassword());
        assertEquals(3306, configuration.getServerSideConfiguration().getDataSource().getPort());

        /* Check socket block */
        assertFalse(configuration.getServerSideConfiguration().getSocket().isEnable());
        assertEquals("127.0.0.1", configuration.getServerSideConfiguration().getSocket().getHost());
        assertEquals((Integer) 2101, configuration.getServerSideConfiguration().getSocket().getPort());
    }


}
