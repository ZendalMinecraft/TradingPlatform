package ru.zendal;

import org.bukkit.plugin.java.JavaPlugin;
import ru.zendal.command.CommandProcessor;
import ru.zendal.event.ChestEvent;
import ru.zendal.session.TradeSessionManager;

public class TradePlace extends JavaPlugin {


    private final TradeSessionManager tradeSessionManager;
    private TradePlaceConfig tradePlaceConfig;

    public TradePlace() {
        tradeSessionManager = new TradeSessionManager();
    }

    @Override
    public void onEnable() {
        this.enableConfig();
        this.getServer().getPluginManager().registerEvents(new ChestEvent(this), this);
        this.getCommand("trade").setExecutor(new CommandProcessor(this));
    }


    private void enableConfig() {
       this.tradePlaceConfig = new TradePlaceConfig(this);
    }


    public TradeSessionManager getSessionManager() {
        return this.tradeSessionManager;
    }
}
