package ru.zendal;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.zendal.command.CommandProcessor;
import ru.zendal.config.AdaptiveMessage;
import ru.zendal.config.TradingPlatformConfig;
import ru.zendal.event.ChestEvent;
import ru.zendal.event.ChestStorageEvent;
import ru.zendal.session.TradeSessionManager;

public class TradingPlatform extends JavaPlugin {


    private TradeSessionManager tradeSessionManager;
    private TradingPlatformConfig tradingPlatformConfig;


    @Override
    public void onEnable() {
        this.enableConfig();
        tradeSessionManager = new TradeSessionManager(tradingPlatformConfig.getLanguageConfig());
        this.initListeners();
        this.getCommand("trade").setExecutor(new CommandProcessor(this));
    }


    private void initListeners(){
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ChestEvent(this), this);
        pluginManager.registerEvents(new ChestStorageEvent(this), this);
    }

    @Override
    public void onDisable() {
        this.getSessionManager().cancelAllSession();
    }

    private void enableConfig() {
       this.tradingPlatformConfig = new TradingPlatformConfig(this);
    }


    public AdaptiveMessage getAdaptiveMessage(String message){
        return  this.tradingPlatformConfig.getLanguageConfig().getMessage(message);
    }


    public TradeSessionManager getSessionManager() {
        return this.tradeSessionManager;
    }


    public TradingPlatformConfig getTradingPlatformConfig() {
        return tradingPlatformConfig;
    }
}
