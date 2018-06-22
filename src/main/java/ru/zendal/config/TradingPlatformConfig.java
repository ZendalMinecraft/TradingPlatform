package ru.zendal.config;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.zendal.TradingPlatform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
     * Instantiates a new Trading platform config.
     *
     * @param plugin the plugin
     */
    public TradingPlatformConfig(TradingPlatform plugin) {
        this.plugin = plugin;
        this.setup();
        this.processConfig();
    }


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
        languageConfig = new LanguageConfig(this.getLanguageFileByName(langName),this.plugin.getLogger());
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
            FileOutputStream fileOutputStream = null;
            try {
                inputStream = this.plugin.getResource(langPath);
                fileOutputStream = new FileOutputStream(langFile);
                //TODO mb remove readAllBytes, for support Java 8
                fileOutputStream.write(inputStream.readAllBytes());
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
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
        this.yamlConfig = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            return;
        }
        if (!file.createNewFile()) {
            this.plugin.getLogger().warning("Failed create config file");
            return;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(this.plugin.getResource("config.yml").readAllBytes());
    }


    /**
     * Gets language config.
     *
     * @return the language config
     */
    public LanguageConfig getLanguageConfig() {
        return this.languageConfig;
    }

}
