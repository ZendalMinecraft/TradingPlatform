/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import ru.zendal.command.CommandManager;
import ru.zendal.config.TradingPlatformConfiguration;
import ru.zendal.event.*;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.socket.SocketServer;
import ru.zendal.util.SchedulerBuilder;

import java.io.File;

public class TradingPlatform extends JavaPlugin {

    private TradeSessionManager tradeSessionManager;
    private SchedulerBuilder schedulerBuilder = new SchedulerBuilder(this);

    private SocketServer socketServer;


    /**
     * Default constructor
     */
    public TradingPlatform() { }

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new TradingPlatformConfiguration(this));
        tradeSessionManager = injector.getInstance(TradeSessionManager.class);

        this.initListeners(injector);
        this.getCommand("trade").setExecutor(injector.getInstance(CommandManager.class));
        this.socketServer = injector.getInstance(SocketServer.class);
    }


    /**
     * Init listeners events
     */
    private void initListeners(Injector injector) {
        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(
                injector.getInstance(ChestTradeSessionEvent.class),
                this
        );

        pluginManager.registerEvents(
                injector.getInstance(ChestStorageEvent.class),
                this
        );

        pluginManager.registerEvents(
                injector.getInstance(PlayerOfflineSessionEvent.class),
                this
        );

        pluginManager.registerEvents(
                injector.getInstance(ChestTradeOfflineEvent.class
                ), this);

        pluginManager.registerEvents(injector.getInstance(InventoryBetPickEvent.class),
                this);

    }

    @Override
    public void onDisable() {
        tradeSessionManager.cancelAllSession();
        if (socketServer != null) {
            socketServer.stop();
        }
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