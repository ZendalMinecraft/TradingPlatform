package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.exception.TradeSessionManagerException;
import ru.zendal.session.inventory.StorageHolderInventory;
import ru.zendal.session.storage.StorageSessions;

import java.util.*;

public class TradeSessionManager {

    private final LanguageConfig languageConfig;
    private final StorageSessions storage;
    private List<TradeSession> sessionList = new ArrayList<>();

    private List<TradeOfflineSession> offlineSessionList = new ArrayList<>();

    /**
     * Storage inventories players
     */
    private HashMap<String, Inventory> storageInventories = new HashMap<>();


    private HashMap<String, TradeOfflineSession> allOfflineSessions = new HashMap<>();

    public TradeSessionManager(StorageSessions storageSessions, LanguageConfig config) {
        this.languageConfig = config;
        this.storage = storageSessions;
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

    public TradeSessionManager createSession(Player player) {
        offlineSessionList.add(new TradeOfflineSession(player, this::onReadyOfflineSession));

        return this;
    }

    private void onReadyOfflineSession(Session session) {
        if (!(session instanceof TradeOfflineSession)) {
            return;
        }
        TradeOfflineSession offlineSession = (TradeOfflineSession) session;
        this.offlineSessionList.remove(offlineSession);
        Bukkit.broadcastMessage("asd");
        storage.saveOfflineSession(offlineSession);
        allOfflineSessions.put(this.getUniqueIdentification(), offlineSession);
    }


    private String getUniqueIdentification() {
        return UUID.randomUUID().toString();
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

    public TradeSessionManager cancelOfflineSessionByPlayer(Player player) throws TradeSessionManagerException {
        for (TradeOfflineSession session : offlineSessionList) {
            if (session.getPlayer() == player) {
                session.cancelTrade();
                offlineSessionList.remove(session);
                return this;
            }
        }
        throw new TradeSessionManagerException("undefined offline session");
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
        if (inventory == null) {
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


    private void onReady(Session tradeSession) {
        if (!(tradeSession instanceof TradeSession)) {
            return;
        }
        //TODO Added check offline player
        //TODO Если у пользователя полный инвентарь, то предметы не добавляются

        TradeSession session = (TradeSession) tradeSession;
        this.addNotFitItemsIntoStorageInventory(
                session.getSeller().getInventory().addItem(
                        session.getBuyerItems().toArray(new ItemStack[0])),
                session.getSeller()
        );
        this.addNotFitItemsIntoStorageInventory(session.getBuyer().getInventory().addItem(
                session.getSellerItems().toArray(new ItemStack[0])),
                session.getBuyer()
        );

        if (session.getBuyer().getOpenInventory().getTopInventory().hashCode() == tradeSession.getInventory().hashCode()) {
            session.getBuyer().closeInventory();
        }
        if (session.getSeller().getOpenInventory().getTopInventory().hashCode() == tradeSession.getInventory().hashCode()) {
            session.getSeller().closeInventory();
        }

        try {
            this.removeSession(session);
        } catch (TradeSessionManagerException e) {
            e.printStackTrace();
        }
    }

    private void addNotFitItemsIntoStorageInventory(Map<Integer, ItemStack> items, Player player) {
        if (items.size() == 0) {
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
        Inventory inventory = Bukkit.createInventory(new StorageHolderInventory(), 54, "Storage " + player.getDisplayName());
        ItemStack itemGetAllItems = new ItemStack(Material.TORCH);
        ItemMeta meta = itemGetAllItems.getItemMeta();
        meta.setDisplayName(this.languageConfig.getMessage("storage.item.getAllItems.caption").toString());
        itemGetAllItems.setItemMeta(meta);
        inventory.setItem(53, itemGetAllItems);
        return inventory;
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

    public TradeOfflineSession getOfflineSessionByPlayer(Player player) throws TradeSessionManagerException {
        for (TradeOfflineSession session : offlineSessionList) {
            if (session.getPlayer() == player) {
                return session;
            }
        }
        throw new TradeSessionManagerException("Undefined offline session");
    }
}
