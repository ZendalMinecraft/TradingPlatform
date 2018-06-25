package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.exception.TradeSessionManagerException;
import ru.zendal.session.inventory.StorageHolderInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeSessionManager {

    private final LanguageConfig languageConfig;
    private List<TradeSession> sessionList = new ArrayList<>();

    /**
     * Storage inventories players
     */
    private HashMap<String, Inventory> storageInventories = new HashMap<>();


    public TradeSessionManager(LanguageConfig config){
        this.languageConfig = config;
        storageInventories.put(Bukkit.getPlayer("gasfull").getUniqueId().toString(),
                this.createStorageInventory(Bukkit.getPlayer("gasfull")));
    }

    public TradeSessionManager addSession(TradeSession session) {
        sessionList.add(session);
        return this;
    }


    public TradeSessionManager createSession(Player seller, Player buyer) {
        Inventory inventory = null;
        if (buyer != null) {
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

    public Inventory getInventorySabotageForPlayer(Player player) throws TradeSessionManagerException {
        Inventory inventory = this.storageInventories.get(player.getUniqueId().toString());
        if (inventory==null){
            throw new TradeSessionManagerException("undefined Player");
        }
        return inventory;
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


    private void onReady(TradeSession tradeSession) {
        //TODO Added check offline player
        //TODO Если у пользователя полный инвентарь, то предметы не добавляются
        this.addNotFitItemsIntoStorageInventory(
                tradeSession.getSeller().getInventory().addItem(
                        tradeSession.getBuyerItems().toArray(new ItemStack[0])),
                tradeSession.getSeller()
        );
        this.addNotFitItemsIntoStorageInventory(tradeSession.getBuyer().getInventory().addItem(
                tradeSession.getSellerItems().toArray(new ItemStack[0])),
                tradeSession.getBuyer()
        );

        if (tradeSession.getBuyer().getOpenInventory().getTopInventory().hashCode() == tradeSession.getInventory().hashCode()) {
            tradeSession.getBuyer().closeInventory();
        }
        if (tradeSession.getSeller().getOpenInventory().getTopInventory().hashCode() == tradeSession.getInventory().hashCode()) {
            tradeSession.getSeller().closeInventory();
        }

        try {
            this.removeSession(tradeSession);
        } catch (TradeSessionManagerException e) {
            e.printStackTrace();
        }
    }

    private void addNotFitItemsIntoStorageInventory(Map<Integer, ItemStack> items, Player player) {
        if (items.size()==0){
            return;
        }
        languageConfig.getMessage("trade.confirm.tooManyItems").sendMessage(player);
        Inventory inventory = storageInventories.get(player.getUniqueId().toString());
        if (inventory == null) {
            inventory = this.createStorageInventory(player);
            storageInventories.put(player.getUniqueId().toString(), inventory);
        }
        Inventory finalInventory = inventory;
        items.forEach((index, item) -> {
            finalInventory.addItem(item);
        });
    }

    private Inventory createStorageInventory(Player player) {
        return Bukkit.createInventory(new StorageHolderInventory(), 54, "Storage " + player.getDisplayName());
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
