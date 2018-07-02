/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.util;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SchedulerBuilder {

    private JavaPlugin pluginInstance;

    public SchedulerBuilder(JavaPlugin plugin) {
        this.pluginInstance = plugin;
    }

    public void runTaskTimer(BukkitRunnable runnable, long delay, long period) {
        runnable.runTaskTimer(pluginInstance, delay, period);
    }


}
