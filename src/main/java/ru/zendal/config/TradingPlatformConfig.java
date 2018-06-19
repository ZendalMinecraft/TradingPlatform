package ru.zendal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TradingPlatformConfig {


    private final TradingPlatform plugin;

    private String[] availableLanguage = new String[]{"english", "russian"};

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
            File langFile = new File(this.plugin.getDataFolder(), langPath);
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

    private void initConfigFile() throws IOException {
        File file = new File(this.plugin.getDataFolder(), "config.yml");
        if (file.exists()) {
            return;
        }
        if (!file.createNewFile()){
            this.plugin.getLogger().warning("Failed create config file");
            return;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(this.plugin.getResource("config.yml").readAllBytes());
    }


    public String getMessage(String message) {
        return "";
    }

}
