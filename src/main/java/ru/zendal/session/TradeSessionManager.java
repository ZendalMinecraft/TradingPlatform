package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.TradeSessionHolderInventory;
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
        sessionList.add(new TradeSession(seller, buyer, this::onReady));
        return this;
    }


    public TradeSession getSessionByBuyer(Player buyer) throws TradeSessionManagerException {
        for (TradeSession session : sessionList) {
            if (session.getBuyer() == buyer) {
                return session;
            }
        }
        throw new TradeSessionManagerException("trade.undefinedSessions");
    }


    public TradeSession getSessionForSellerAndBuyer(Player seller, Player buyer) throws TradeSessionManagerException {
        for (TradeSession session : sessionList) {
            if (session.getSeller() == seller &&
                    session.getBuyer() == buyer) {
                return session;
            }
        }
        throw new TradeSessionManagerException("trade.confirm.select.undefinedSession");
    }

    public TradeSession getSessionForSellerOrBuyer(Player seller, Player buyer) throws TradeSessionManagerException {
        for (TradeSession session : sessionList) {
            if ((session.getSeller() == seller && session.getBuyer() == buyer) ||
                    (session.getSeller() == buyer && session.getBuyer() == seller)) {
                return session;
            }
        }
        throw new TradeSessionManagerException("trade.confirm.select.undefinedSession");
    }


    public List<TradeSession> getAllAvailableSessionsForBuyer(Player buyer) {
        List<TradeSession> tradeSessions = new ArrayList<>();
        for (TradeSession tradeSession : sessionList) {
            if (tradeSession.getBuyer() == buyer) {
                tradeSessions.add(tradeSession);
            }
        }
        return tradeSessions;
    }

    public int getCountSessionsBuyer(Player buyer) {
        int count = 0;
        for (TradeSession session : sessionList) {
            if (session.getBuyer() == buyer) {
                count++;
            }
        }
        return count;
    }

    public int getCountSessionsSeller(Player seller) {
        int count = 0;
        for (TradeSession session : sessionList) {
            if (session.getSeller() == seller) {
                count++;
            }
        }
        return count;
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
        Inventory inventory = Bukkit.createInventory(new TradeSessionHolderInventory(), 9 * 6, "Trade " + seller.getDisplayName() + " and " + buyer.getDisplayName());
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
        //TODO Added check offline player
        //TODO Если у пользователя полный инвентарь, то предметы не добавляются
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


    public void cancelAllSession() {
        if (sessionList.size() == 0) {
            return;
        }
        for (TradeSession tradeSession : sessionList) {
            tradeSession.getSeller().getInventory().addItem(tradeSession.getSellerItems().toArray(new ItemStack[0]));
            tradeSession.getBuyer().getInventory().addItem(tradeSession.getBuyerItems().toArray(new ItemStack[0]));
            tradeSession.getBuyer().closeInventory();
            tradeSession.getSeller().closeInventory();
        }
    }
}
