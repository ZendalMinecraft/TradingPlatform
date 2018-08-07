/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import ru.zendal.command.CommandProcessor;
import ru.zendal.config.TradingPlatformConfig;
import ru.zendal.config.bundle.SocketConfigBundle;
import ru.zendal.event.*;
import ru.zendal.service.economy.EconomyProvider;
import ru.zendal.service.economy.VaultEconomy;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.storage.MongoStorageSessions;
import ru.zendal.session.storage.connection.builder.MongoConnectionBuilder;
import ru.zendal.socket.SocketIO;
import ru.zendal.socket.SocketServer;
import ru.zendal.util.SchedulerBuilder;

import java.io.File;

public class TradingPlatform extends JavaPlugin {


    private TradeSessionManager tradeSessionManager;
    private TradingPlatformConfig tradingPlatformConfig;
    private SchedulerBuilder schedulerBuilder = new SchedulerBuilder(this);

    private SocketServer socketServer;

    private EconomyProvider economyProvider;

    /**
     * Default constructor
     */
    public TradingPlatform() {
        super();
    }

    @Override
    public void onEnable() {

        this.enableConfig();
        //TODO Возможно такая ситуация, когда Economy Provider недоступен УЧТИ
        tradeSessionManager = new TradeSessionManager(new MongoStorageSessions(
                new MongoConnectionBuilder(), getLogger()
        ), this, tradingPlatformConfig.getLanguageConfig());
        this.initService();
        this.initListeners();
        this.getCommand("trade").setExecutor(new CommandProcessor(
                tradeSessionManager,
                tradingPlatformConfig.getLanguageConfig())
        );
        this.initSocketServer();
    }


    /**
     * Init listeners events
     */
    private void initListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(
                new ChestTradeSessionEvent(
                        tradeSessionManager,
                        tradingPlatformConfig.getLanguageConfig(),
                        tradingPlatformConfig.getBetSpread()
                ), this);

        pluginManager.registerEvents(
                new ChestStorageEvent(this), this);

        pluginManager.registerEvents(
                new PlayerOfflineSessionEvent(
                        tradeSessionManager, tradingPlatformConfig.getLanguageConfig()
                ), this);

        pluginManager.registerEvents(
                new ChestTradeOfflineEvent(
                        tradeSessionManager, tradingPlatformConfig.getLanguageConfig()
                ), this);

        pluginManager.registerEvents(new InventoryBetPickEvent(economyProvider),
                this);

    }

    /**
     * Init socket server
     */
    private void initSocketServer() {
        SocketConfigBundle configBundle = tradingPlatformConfig.getSocketBundle();
        if (configBundle.isEnableServer()) {
            this.getLogger().info("Start init Socket server");
            socketServer = new SocketIO(configBundle, tradeSessionManager, getLogger());
            if (!socketServer.start()) {
                this.getLogger().warning("Can't start SocketServer. Configuration: " +
                        configBundle.toString());
            }
        }
    }


    private void initService() {
        economyProvider = new VaultEconomy(getServer());
    }

    @Override
    public void onDisable() {
        tradeSessionManager.cancelAllSession();
        if (socketServer != null) {
            socketServer.stop();
        }
    }

    private void enableConfig() {
        this.tradingPlatformConfig = new TradingPlatformConfig(this);
    }


    /**
     * Constructor for MockBukkit
     *
     * @param loader      Java plugin loader
     * @param description plguin description
     * @param dataFolder  data folder
     * @param file        File
     */
    protected TradingPlatform(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

}
