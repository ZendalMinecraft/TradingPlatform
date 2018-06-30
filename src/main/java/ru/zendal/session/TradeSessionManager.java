/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.zendal.TradingPlatform;
import ru.zendal.config.LanguageConfig;
import ru.zendal.session.exception.TradeSessionManagerException;
import ru.zendal.session.inventory.CreateOfflineTradeHolderInventory;
import ru.zendal.session.inventory.StorageHolderInventory;
import ru.zendal.session.storage.StorageSessions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Trade session manager.
 */
public class TradeSessionManager {

    private final LanguageConfig languageConfig;
    private final StorageSessions storage;
    private final TradingPlatform plugin;
    private final TradeSessionCallback tradeCallback;

    /**
     * Storage simple trade sessions
     */
    private List<TradeSession> sessionList = new ArrayList<>();

    /**
     * Storage trade offline sessions
     */
    private List<TradeOfflineSession> offlineSessionList = new ArrayList<>();

    /**
     * Storage inventories players, for store item's not fit TODO Better English PLS
     */
    private HashMap<String, Inventory> storageInventories = new HashMap<>();

    /**
     * Storage all active offline sessions
     */
    private HashMap<String, TradeOffline> activeOfflineTrade = new HashMap<>();

    /**
     * Constructor for Trade Session Manager
     *
     * @param storageSessions Storage where manager can store data
     * @param plugin          Instance Plugin
     * @param config          Config language Pack
     */
    public TradeSessionManager(StorageSessions storageSessions, TradingPlatform plugin, LanguageConfig config) {
        this.languageConfig = config;
        this.storage = storageSessions;
        this.plugin = plugin;
        this.tradeCallback = new TradeSessionCallback() {
            @Override
            public void onReady(Session tradeSession) {
                if (tradeSession instanceof TradeSession) {
                    ((TradeSession) tradeSession).enableTimer(plugin);
                }
            }

            @Override
            public void processTrade(Session tradeSession) {
                processTradeSession(tradeSession);
            }
        };
        this.initAllTradeOfflineSessions();
    }

    /**
     * Init all trade offline sessions from Storage
     *
     * @see TradeOffline
     */
    private void initAllTradeOfflineSessions() {
        for (TradeOffline tradeOffline : storage.getAllSessions()) {
            activeOfflineTrade.put(tradeOffline.getUniqueId(), tradeOffline);
        }
    }

    /**
     * Create simple session
     *
     * @param seller Player Seller - who start trade (Session creator)
     * @param buyer  Player buyer - who accept trade
     */
    public void createSession(Player seller, Player buyer) {
        sessionList.add(new TradeSession(seller, buyer, tradeCallback));
    }

    /**
     * Create offline trade session
     *
     * @param player Player (Session creator)
     */
    public void createOfflineSession(Player player) {
        if (!storage.isAvailable()) {
            languageConfig.getMessage("trade.offline.unavailable").sendMessage(player);
        } else {
            offlineSessionList.add(new TradeOfflineSession(player, tradeCallback));
        }
    }


    private void processTradeSession(Session tradeSession) {
        if (tradeSession instanceof TradeOfflineSession) {
            this.processOfflineTrade((TradeOfflineSession) tradeSession);
        } else if (tradeSession instanceof TradeSession) {
            this.processTrade((TradeSession) tradeSession);
        }
        //TODO added Logger Undefined tradeSessions....
    }

    /**
     * Process Offline trade session
     *
     * @param session Session
     * @see TradeOfflineSession
     */
    private void processOfflineTrade(TradeOfflineSession session) {
        try {
            String uniqueId = storage.saveSession(session);
            activeOfflineTrade.put(uniqueId, TradeOffline.factory(uniqueId, session));
            languageConfig.getMessage("trade.offline.process").
                    setCustomMessage(1, uniqueId).sendMessage(session.getBuyer());
        } catch (Exception exception) {
            languageConfig.getMessage("trade.offline.unavailable").sendMessage(session.getBuyer());
            session.cancelTrade();
        }
        this.offlineSessionList.remove(session);
    }

    /**
     * Process simple session trade
     *
     * @param session Session
     * @see TradeSession
     */
    private void processTrade(TradeSession session) {
        this.addNotFitItemsIntoStorageInventory(
                session.getSeller().getInventory().addItem(
                        session.getBuyerItems().toArray(new ItemStack[0])),
                session.getSeller()
        );
        this.addNotFitItemsIntoStorageInventory(session.getBuyer().getInventory().addItem(
                session.getSellerItems().toArray(new ItemStack[0])),
                session.getBuyer()
        );

        if (session.getBuyer().getOpenInventory().getTopInventory().hashCode() == session.getInventory().hashCode()) {
            session.getBuyer().closeInventory();
        }
        if (session.getSeller().getOpenInventory().getTopInventory().hashCode() == session.getInventory().hashCode()) {
            session.getSeller().closeInventory();
        }

        try {
            this.removeSession(session);
        } catch (TradeSessionManagerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get simple Session trade by Buyer
     *
     * @param buyer Player who accept trade
     * @return simple Session
     * @throws TradeSessionManagerException if session not founded
     * @see TradeSession
     */
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

    public void cancelOfflineSession(TradeOfflineSession session) {
        this.offlineSessionList.remove(session);
        session.cancelTrade();
    }

    public TradeSessionManager cancelOfflineSessionByPlayer(Player player) throws TradeSessionManagerException {
        for (TradeOfflineSession session : offlineSessionList) {
            if (session.getBuyer() == player || session.getSeller() == player) {
                //TODO do...
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

    public Session getSessionByInventory(Inventory inventory) throws TradeSessionManagerException {
        if (inventory.getHolder() instanceof CreateOfflineTradeHolderInventory) {
            for (TradeOfflineSession session : offlineSessionList) {
                if (session.getInventory().hashCode() == inventory.hashCode()) {
                    return session;
                }
            }
        }
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

    private void addNotFitItemsIntoStorageInventory(Map<Integer, ItemStack> items, OfflinePlayer player) {
        if (items.size() == 0) {
            return;
        }
        Inventory inventory = storageInventories.get(player.getUniqueId().toString());
        if (inventory == null) {
            inventory = this.createStorageInventory(player);
            storageInventories.put(player.getUniqueId().toString(), inventory);
        }
        Inventory finalInventory = inventory;
        items.forEach((index, item) -> {
            finalInventory.addItem(item);
        });
        if (player.isOnline()) {
            languageConfig.getMessage("trade.confirm.tooManyItems").sendMessage(player.getPlayer());
        }
    }

    private Inventory createStorageInventory(OfflinePlayer player) {
        Inventory inventory = Bukkit.createInventory(new StorageHolderInventory(), 54, "Storage " + player.getName());
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
        tradeSession.setReadyBuyer(false);
        tradeSession.setReadySeller(false);
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
            if (session.getSeller() == player || session.getBuyer() == player) {
                return session;
            }
        }
        throw new TradeSessionManagerException("Undefined offline session");
    }

    public TradeOffline getTradeOfflineById(String id) throws TradeSessionManagerException {
        for (Map.Entry<String, TradeOffline> entry : activeOfflineTrade.entrySet()) {
            if (entry.getKey().equals(id)) {
                return entry.getValue();
            }
        }
        throw new TradeSessionManagerException("Undefined Trade offline by id");
    }

    public TradeOffline getTradeOfflineByInventory(Inventory inventory) throws TradeSessionManagerException {
        for (Map.Entry<String, TradeOffline> entry : activeOfflineTrade.entrySet()) {
            if (entry.getValue().getInventory().hashCode() == inventory.hashCode()) {
                return entry.getValue();
            }
        }
        throw new TradeSessionManagerException("Undefined Trade offline by id");
    }
}
