/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.event;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EventRegister {

    private final PluginManager pluginManager;
    private final JavaPlugin plugin;

    public EventRegister(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        this.pluginManager = javaPlugin.getServer().getPluginManager();

    }


    private void initEvents(){

    }

    public void registerEventClass(Listener listener) {
        this.pluginManager.registerEvents(listener, plugin);
    }
}
