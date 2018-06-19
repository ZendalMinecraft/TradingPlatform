package ru.zendal;

import org.bukkit.plugin.java.JavaPlugin;
import ru.zendal.command.CommandProcessor;
import ru.zendal.config.TradingPlatformConfig;
import ru.zendal.event.ChestEvent;
import ru.zendal.session.TradeSessionManager;

public class TradingPlatform extends JavaPlugin {


    private final TradeSessionManager tradeSessionManager;
    private TradingPlatformConfig tradingPlatformConfig;

    public TradingPlatform() {
        tradeSessionManager = new TradeSessionManager();
    }

    @Override
    public void onEnable() {
        this.enableConfig();
        this.getServer().getPluginManager().registerEvents(new ChestEvent(this), this);
        this.getCommand("trade").setExecutor(new CommandProcessor(this));
    }


    private void enableConfig() {
       this.tradingPlatformConfig = new TradingPlatformConfig(this);
    }


    public TradeSessionManager getSessionManager() {
        return this.tradeSessionManager;
    }


    public TradingPlatformConfig getTradingPlatformConfig() {
        return tradingPlatformConfig;
    }
}
