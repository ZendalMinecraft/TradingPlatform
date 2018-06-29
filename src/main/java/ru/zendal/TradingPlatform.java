package ru.zendal;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.zendal.command.CommandProcessor;
import ru.zendal.config.AdaptiveMessage;
import ru.zendal.config.TradingPlatformConfig;
import ru.zendal.event.ChestStorageEvent;
import ru.zendal.event.ChestTradeSessionEvent;
import ru.zendal.event.PlayerOfflineSessionEvent;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.economy.VaultEconomy;
import ru.zendal.session.storage.MongoStorageSessions;
import ru.zendal.session.storage.connection.builder.MongoConnectionBuilder;

public class TradingPlatform extends JavaPlugin {


    private TradeSessionManager tradeSessionManager;
    private TradingPlatformConfig tradingPlatformConfig;

    private Economy economy;

    @Override
    public void onEnable() {
        this.enableConfig();
        tradeSessionManager = new TradeSessionManager(new MongoStorageSessions(
                new MongoConnectionBuilder()
        ), new VaultEconomy(this), this, tradingPlatformConfig.getLanguageConfig());
        this.initListeners();
        this.getCommand("trade").setExecutor(new CommandProcessor(this));
        System.out.println(setupEconomy());
    }


    private void initListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ChestTradeSessionEvent(this), this);
        pluginManager.registerEvents(new ChestStorageEvent(this), this);
        pluginManager.registerEvents(new PlayerOfflineSessionEvent(this), this);
    }


    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    @Override
    public void onDisable() {
        this.getSessionManager().cancelAllSession();
    }

    private void enableConfig() {
        this.tradingPlatformConfig = new TradingPlatformConfig(this);
    }


    public AdaptiveMessage getAdaptiveMessage(String message) {
        return this.tradingPlatformConfig.getLanguageConfig().getMessage(message);
    }


    public TradeSessionManager getSessionManager() {
        return this.tradeSessionManager;
    }


    public TradingPlatformConfig getTradingPlatformConfig() {
        return tradingPlatformConfig;
    }
}
