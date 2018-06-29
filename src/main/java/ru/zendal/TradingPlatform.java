/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.zendal.command.CommandProcessor;
import ru.zendal.config.AdaptiveMessage;
import ru.zendal.config.TradingPlatformConfig;
import ru.zendal.event.ChestStorageEvent;
import ru.zendal.event.ChestTradeOfflineEvent;
import ru.zendal.event.ChestTradeSessionEvent;
import ru.zendal.event.PlayerOfflineSessionEvent;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.storage.MongoStorageSessions;
import ru.zendal.session.storage.connection.builder.MongoConnectionBuilder;

public class TradingPlatform extends JavaPlugin {


    private TradeSessionManager tradeSessionManager;
    private TradingPlatformConfig tradingPlatformConfig;


    @Override
    public void onEnable() {
        this.enableConfig();
        //new NitriteStorageSessions(new NitriteConnectionBuilder());
        tradeSessionManager = new TradeSessionManager(new MongoStorageSessions(
                new MongoConnectionBuilder(), getLogger()
        ), this, tradingPlatformConfig.getLanguageConfig());
        this.initListeners();
        this.getCommand("trade").setExecutor(new CommandProcessor(this));
    }


    private void initListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ChestTradeSessionEvent(this), this);
        pluginManager.registerEvents(new ChestStorageEvent(this), this);
        pluginManager.registerEvents(new PlayerOfflineSessionEvent(this), this);
        pluginManager.registerEvents(new ChestTradeOfflineEvent(this), this);
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
