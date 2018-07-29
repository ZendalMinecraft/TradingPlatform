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
import ru.zendal.config.AdaptiveMessage;
import ru.zendal.config.TradingPlatformConfig;
import ru.zendal.config.bundle.SocketConfigBundle;
import ru.zendal.event.ChestStorageEvent;
import ru.zendal.event.ChestTradeOfflineEvent;
import ru.zendal.event.ChestTradeSessionEvent;
import ru.zendal.event.PlayerOfflineSessionEvent;
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

    /**
     * Default constructor
     */
    public TradingPlatform() {
        super();
    }

    @Override
    public void onEnable() {
        this.enableConfig();
        tradeSessionManager = new TradeSessionManager(new MongoStorageSessions(
                new MongoConnectionBuilder(), getLogger()
        ), this, tradingPlatformConfig.getLanguageConfig());
        this.initListeners();
        this.getCommand("trade").setExecutor(new CommandProcessor(this));
        this.initSocketServer();
    }


    /**
     * Init listeners events
     */
    private void initListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ChestTradeSessionEvent(this), this);
        pluginManager.registerEvents(new ChestStorageEvent(this), this);
        pluginManager.registerEvents(new PlayerOfflineSessionEvent(this), this);
        pluginManager.registerEvents(new ChestTradeOfflineEvent(getSessionManager(), getTradingPlatformConfig().getLanguageConfig()), this);

    }

    /**
     * Init socket server
     */
    private void initSocketServer(){
        SocketConfigBundle configBundle = tradingPlatformConfig.getSocketBundle();
        socketServer = new SocketIO(configBundle, tradeSessionManager,getLogger());
        if (!socketServer.start()){
            this.getLogger().warning("Can't start SocketServer. Configuration: "+
                    configBundle.toString());
        }

    }


    @Override
    public void onDisable() {
        this.getSessionManager().cancelAllSession();
        socketServer.stop();
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
