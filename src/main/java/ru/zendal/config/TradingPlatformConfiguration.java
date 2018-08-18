/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import ru.zendal.TradingPlatform;
import ru.zendal.config.bundle.InventoryConfigBundle;
import ru.zendal.config.bundle.SocketConfigBundle;
import ru.zendal.service.economy.EconomyProvider;
import ru.zendal.service.economy.VaultEconomy;
import ru.zendal.session.storage.MongoStorageSessions;
import ru.zendal.session.storage.PacifierStorage;
import ru.zendal.session.storage.SessionsStorage;
import ru.zendal.session.storage.connection.builder.MongoConnectionBuilder;
import ru.zendal.socket.SocketIO;
import ru.zendal.socket.SocketServer;

import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Class for access to Config file plugin
 */
@Singleton
public class TradingPlatformConfiguration extends AbstractModule {

    /**
     * Instance plugin file
     */
    private final TradingPlatform plugin;

    /**
     * Logger
     */
    private final Logger logger;

    /**
     * Available language for restore from plugin
     */
    private String[] availableLanguage = new String[]{"english", "russian"};

    /**
     * Instance config file
     */
    private YamlConfiguration yamlConfig;

    /**
     * Instance language config
     */
    private LanguageConfig languageConfig;


    /**
     * Socket configuration data
     */
    private SocketConfigBundle socketBundle;


    private SessionsStorage sessionsStorage;

    /**
     * Instantiates a new Trading platform config.
     *
     * @param plugin the plugin
     */
    @Inject
    public TradingPlatformConfiguration(TradingPlatform plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.setup();
        this.processConfig();
    }

    /**
     * Init config files
     */
    private void setup() {
        try {
            this.checkFolder();
            this.checkAllLanguage();
            this.initConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processConfig() {
        this.initLanguage();
    }

    private void initLanguage() {
        String langName = this.yamlConfig.getString("settings.lang");
        languageConfig = new LanguageConfig(this.getLanguageFileByName(langName), this.plugin.getLogger());
    }

    /**
     * Init folders
     */
    private void checkFolder() {
        if (this.plugin.getDataFolder().exists()) {
            return;
        }
        this.initDefaultFolders();
    }

    private void initDefaultFolders() {
        if (!new File(this.plugin.getDataFolder(), "lang").mkdirs()) {
            this.plugin.getLogger().warning("Can't create folder config");
        }
    }


    @Override
    protected void configure() {
        bind(EconomyProvider.class).to(VaultEconomy.class);
        bind(SocketServer.class).to(SocketIO.class);
    }

    private void checkAllLanguage() throws IOException {
        for (String lang : this.availableLanguage) {
            String langPath = "lang/" + lang + ".lang";
            File langFile = this.getLanguageFileByName(lang);
            if (langFile.exists()) {
                continue;
            }
            InputStream inputStream = null;
            try {
                inputStream = this.plugin.getResource(langPath);
                langFile.getParentFile().mkdirs();

                PrintWriter writer = new PrintWriter(langFile);
                writer.print(this.getStringByInputStream(this.plugin.getResource(langPath)));
                writer.close();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void initConfigFile() throws IOException {
        File file = new File(this.plugin.getDataFolder(), "config.yml");
        if (file.exists()) {
            this.yamlConfig = YamlConfiguration.loadConfiguration(file);
            return;
        }
        if (!file.createNewFile()) {
            this.plugin.getLogger().warning("Failed create config file");
            return;
        }
        PrintWriter writer = new PrintWriter(file);
        writer.print(this.getStringByInputStream(this.plugin.getResource("config.yml")));
        writer.close();
        this.yamlConfig = YamlConfiguration.loadConfiguration(file);
    }


    /**
     * Gets language config.
     *
     * @return the language config
     */
    public LanguageConfig getLanguageConfig() {
        return this.languageConfig;
    }


    private List<Double> getBetSpread() {
        if (yamlConfig.contains("settings.betSpread")) {
            List<Double> betSpread = yamlConfig.getDoubleList("settings.betSpread");
            if (betSpread.size() == 0 || betSpread.size() > 8) {
                logger.warning("Bad config bet Spread");
                return new ArrayList<>();
            }
            return betSpread;
        }
        logger.warning("Bad config bet Spread");
        return new ArrayList<>();
    }


    private void initSocketBundle() {
        socketBundle = new SocketConfigBundle();
        if (yamlConfig.contains("socket.enable")) {
            socketBundle.setEnableServer(yamlConfig.getBoolean("socket.enable"));
        }
        if (yamlConfig.contains("socket.port")) {
            socketBundle.setPort(yamlConfig.getInt("socket.port"));
        }

        if (yamlConfig.contains("socket.charset")) {
            socketBundle.setCharset(yamlConfig.getString("socket.charset"));
        }
    }


    /**
     * Get Mongo storage
     *
     * @return MongoDB Storage
     */
    private SessionsStorage getMongoStorage() {
        MongoConnectionBuilder builder = new MongoConnectionBuilder();
        if (yamlConfig.contains("storage.setting.host")) {
            builder.setHost(yamlConfig.getString("storage.setting.host"));
        }

        if (yamlConfig.contains("storage.setting.port")) {
            builder.setPort(yamlConfig.getInt("storage.setting.port"));
        }
        return new MongoStorageSessions(builder, logger);
    }


    /**
     * Get text By InputStream
     *
     * @param inputStream Input Stream
     * @return Text
     * @throws IOException on Error read
     */
    private String getStringByInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }


    /**
     * Get Mongo storage
     *
     * @return MongoDB Storage
     */
    @Provides
    @Singleton
    private SessionsStorage provideSessionsStorage() {
        if (!yamlConfig.contains("storage.type") || !TypeStorage.hasTypeStorage(yamlConfig.getString("storage.type"))) {
            logger.warning("Store type not recognised, use the Stub");
            sessionsStorage = new PacifierStorage();
        } else {
            TypeStorage typeStorage = TypeStorage.fromName(yamlConfig.getString("storage.type"));
            switch (Objects.requireNonNull(typeStorage)) {
                case MONGO_DB:
                    sessionsStorage = this.getMongoStorage();
            }
        }
        return sessionsStorage;
    }

    @Provides
    PluginManager providePluginManager() {
        return this.plugin.getServer().getPluginManager();
    }


    @Provides
    @Singleton
    ServicesManager provideServicesManager() {
        return this.plugin.getServer().getServicesManager();
    }

    /**
     * Provider Language Config
     *
     * @return LanguageConfig
     */
    @Provides
    @Singleton
    LanguageConfig providerLanguageConfig() {
        String langName = this.yamlConfig.getString("settings.lang");
        return new LanguageConfig(this.getLanguageFileByName(langName), this.plugin.getLogger());
    }

    /**
     * Get Language file.
     *
     * @param langName Language name file.
     * @return File.
     */
    private File getLanguageFileByName(String langName) {
        return new File(this.plugin.getDataFolder(), "lang/" + langName + ".lang");
    }

    @Provides
    @Singleton
    TradingPlatform providerPlugin() {
        return this.plugin;
    }

    @Provides
    @Singleton
    InventoryConfigBundle providerInventoryConfigBundle() {
        return new InventoryConfigBundle(this.getBetSpread());
    }


    /**
     * Return socket Bundle
     *
     * @return Socket configuration data
     */
    @Provides
    @Singleton
    public SocketConfigBundle providerSocketBundle() {
        this.initSocketBundle();
        return socketBundle;
    }


}
