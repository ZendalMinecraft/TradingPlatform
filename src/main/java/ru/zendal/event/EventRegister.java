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
import ru.zendal.TradingPlatform;
import ru.zendal.event.exception.CreateListenerException;
import ru.zendal.event.exception.EventRegisterException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class EventRegister {

    private final PluginManager pluginManager;

    private List<Object> dependencyList = new ArrayList<>();

    public EventRegister(JavaPlugin javaPlugin) {
        if (!(javaPlugin instanceof TradingPlatform)){
            throw new EventRegisterException("Sorry EventRegister, don't support this type Java Plugin");
        }

        dependencyList.add(((TradingPlatform) javaPlugin).getSessionManager());

        dependencyList.add(((TradingPlatform) javaPlugin).getTradingPlatformConfig().getLanguageConfig());

        pluginManager = javaPlugin.getServer().getPluginManager();
    }




    private void initEvents(){
        /*pluginManager.registerEvents(new ChestTradeSessionEvent(plugin), plugin);
        pluginManager.registerEvents(new ChestStorageEvent(plugin), plugin);
        pluginManager.registerEvents(new PlayerOfflineSessionEvent(plugin), plugin);
        pluginManager.registerEvents(new ChestTradeOfflineEvent(getSessionManager(), getTradingPlatformConfig().getLanguageConfig()), plugin);*/
    }


    

    public void registerEventClass(Listener listener) {
      /*
        listene
        
        this.pluginManager.registerEvents(listener, plugin);*/
    }
    
    
    public void registerEventClass(Class clazz){
        Constructor[] constructors = clazz.getConstructors();
        if (constructors.length<1){

        }
    }

}
