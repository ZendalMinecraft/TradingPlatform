package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.ServerHolderInventory;
import ru.zendal.session.exception.TradeSessionManagerException;

import java.util.ArrayList;
import java.util.List;

public class TradeSessionManager {

    private List<TradeSession> sessionList = new ArrayList<>();


    public TradeSessionManager addSession(TradeSession session) {
        sessionList.add(session);
        return this;
    }


    public TradeSessionManager createSession(Player seller, Player buyer) {
        Inventory inventory = null;
        if (buyer != null) {
            inventory = createTradeInventory(seller, buyer);
        }
        sessionList.add(new TradeSession(inventory, seller, buyer, this::onReady));
        return this;
    }


    public TradeSession getSessionByBuyer(Player player) throws TradeSessionManagerException {
        for (TradeSession session : sessionList) {
            if (session.getBuyer() == player) {
                return session;
            }
        }
        throw new TradeSessionManagerException("UndefinedSession");
    }


    public TradeSession getSessionByInventory(Inventory inventory) throws TradeSessionManagerException {
        for (TradeSession session : sessionList) {
            if (session.getInventory().hashCode() == inventory.hashCode()) {
                return session;
            }
        }
        throw new TradeSessionManagerException("UndefinedSession");
    }

    public void removeSession(TradeSession session) throws TradeSessionManagerException {
        for (TradeSession tradeSession : sessionList) {
            if (session == tradeSession) {
                sessionList.remove(tradeSession);
                return;
            }
        }
        throw new TradeSessionManagerException("UndefinedSession");
    }

    /**
     * Create Inventory for trade between players
     *
     * @param seller Who create trade
     * @param buyer  Who accept trade
     * @return Inventory for trading
     */
    private Inventory createTradeInventory(Player seller, Player buyer) {
        Inventory inventory = Bukkit.createInventory(new ServerHolderInventory(), 9 * 6, "Trade " + seller.getDisplayName() + " and " + buyer.getDisplayName());
        ItemStack stick = new ItemStack(Material.STICK);
        for (int i = 0; i < 6; i++) {
            inventory.setItem(9 * i + 4, stick);
        }

        ItemStack redWool = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemStack greenWool = new ItemStack(Material.WOOL, 1, (short) 5);

        inventory.setItem(9 * 4 + 4, redWool);
        inventory.setItem(9 * 1 + 4, greenWool);

        return inventory;
    }


    private void onReady(TradeSession tradeSession) {
        tradeSession.getSeller().getInventory().addItem(tradeSession.getBuyerItems().toArray(new ItemStack[0]));
        tradeSession.getBuyer().getInventory().addItem(tradeSession.getSellerItems().toArray(new ItemStack[0]));
        tradeSession.getBuyer().closeInventory();
        tradeSession.getSeller().closeInventory();
        try {
            this.removeSession(tradeSession);
        } catch (TradeSessionManagerException e) {
            e.printStackTrace();
        }
    }


    public void cancelSession(TradeSession tradeSession) {
        tradeSession.getSeller().getInventory().addItem(tradeSession.getSellerItems().toArray(new ItemStack[0]));
        tradeSession.getBuyer().getInventory().addItem(tradeSession.getBuyerItems().toArray(new ItemStack[0]));
        tradeSession.getBuyer().closeInventory();
        tradeSession.getSeller().closeInventory();
        try {
            this.removeSession(tradeSession);
        } catch (TradeSessionManagerException e) {
            e.printStackTrace();
        }
    }
}
