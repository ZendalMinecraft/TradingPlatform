package ru.zendal;

public class TradePlaceConfig {


    private final TradePlace plugin;

    public TradePlaceConfig(TradePlace plugin) {
        this.plugin = plugin;
        this.setup();
    }


    private void setup() {
        this.checkFolder();
        plugin.getDataFolder();
    }

    private void checkFolder() {
        if (this.plugin.getDataFolder().exists()) {
            return;
        }

        this.plugin.getDataFolder().mkdirs();


    }


    private void setupDefaultSettings() {

    }


    public String getMessage(String message) {
        return "";
    }

}
