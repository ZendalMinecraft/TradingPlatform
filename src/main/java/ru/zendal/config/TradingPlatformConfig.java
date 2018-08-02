/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.zendal.TradingPlatform;
import ru.zendal.config.bundle.SocketConfigBundle;
import ru.zendal.config.exception.ConfigException;
import ru.zendal.session.storage.PacifierStorage;
import ru.zendal.session.storage.SessionsStorage;

import java.io.*;

/**
 * Class for access to Config file plugin
 */
public class TradingPlatformConfig {

    /**
     * Instance plugin file
     */
    private final TradingPlatform plugin;

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
    public TradingPlatformConfig(TradingPlatform plugin) {
        this.plugin = plugin;
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


    private File getLanguageFileByName(String langName) {
        return new File(this.plugin.getDataFolder(), "lang/" + langName + ".lang");
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


    public SessionsStorage getStorageByConfig() throws ConfigException {
        String typeStorageString = yamlConfig.getString("storage.type");
        if (typeStorageString.equalsIgnoreCase("mongo")) {
           /* return  new MongoStorageSessions(
                    new MongoConnectionBuilder().setHost();
            )*/
        } else {
            throw new ConfigException("Undefined type config");
        }
        return null;
    }

    /**
     * Return socket Bundle
     *
     * @return Socket configuration data
     */
    public SocketConfigBundle getSocketBundle() {

        if (socketBundle == null) {
            this.initSocketBundle();
        }
        return socketBundle;
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

    public SessionsStorage getSessionsStorage() {
        if (sessionsStorage == null) {
            if (!yamlConfig.contains("storage.type") || !TypeStorage.hasTypeStorage(yamlConfig.getString("storage.type"))) {
                return new PacifierStorage();
            }
        }
    }


    private SessionsStorage initSessionsStorage(){

    }
    /**
     * Check is valid type storage
     *
     * @param nameType Name type storage
     * @return {@code true} if valid
     */
    private boolean isInvalidTypeStorage(String nameType) {
        switch (nameType.toLowerCase()) {
            case "mongo":
            case "mysql":
            case "local":
                return true;
            default:
                return false;
        }
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

}
