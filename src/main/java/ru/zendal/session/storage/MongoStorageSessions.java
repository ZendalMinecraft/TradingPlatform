/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.storage;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import ru.zendal.session.TradeOffline;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.storage.connection.builder.MongoConnectionBuilder;
import ru.zendal.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

/**
 * NoSQL Storage (MongoDB)
 */
public class MongoStorageSessions implements SessionsStorage {

    /**
     * Object Mongo DataBase
     *
     * @see MongoDatabase
     */
    private final MongoDatabase dataBase;

    /**
     * Builder Mongo Connection
     *
     * @see MongoConnectionBuilder
     */
    private final MongoConnectionBuilder builder;

    /**
     * Logger
     *
     * @see Logger
     */
    private final Logger logger;

    /**
     * Collection trades
     */
    private final MongoCollection<Document> tradesCollection;

    /**
     * Constructor
     *
     * @param builder Builder Mongo connection
     * @param logger  Logger
     * @see Logger
     * @see MongoConnectionBuilder
     */
    public MongoStorageSessions(MongoConnectionBuilder builder, Logger logger) {
        this.builder = builder;
        this.logger = logger;
        dataBase = builder.build();
        this.initCollections();
        tradesCollection = dataBase.getCollection("trades");
    }

    /**
     * Initialize collections
     */
    private void initCollections() {
        if (!hasCollection("trades")) {
            dataBase.createCollection("trades");
        }
    }


    /**
     * Check exists Collection
     *
     * @param nameCollection Name Collection
     * @return {@code true} if exists else {@code false}
     */
    private boolean hasCollection(String nameCollection) {
        try {
            dataBase.getCollection(nameCollection);
            return true;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    @Override
    public String saveSession(TradeOfflineSession session) {
        return this.processOfflineSession(session);
    }

    /**
     * Send TradeOfflineSession to mongo Storage
     *
     * @param session TradeOfflineSession
     * @return Unique ID record
     * @see TradeOfflineSession
     */
    private String processOfflineSession(TradeOfflineSession session) {
        Document data = new Document();
        data.append("uuidPlayer", session.getBuyer().getUniqueId().toString());
        data.append("playerItems", this.getListDocumentByArrayItemStack(session.getSellerItems().toArray(new ItemStack[0])));
        data.append("playerBet", session.getBetSeller());
        data.append("itemsWant", this.getListDocumentByArrayItemStack(session.getBuyerItems().toArray(new ItemStack[0])));
        data.append("betWant", session.getBetBuyer());
        tradesCollection.insertOne(data);
        return data.get("_id").toString();
    }

    @Override
    public List<TradeOffline> getAllSessions() {
        List<TradeOffline> response = new ArrayList<>();
        for (Document data : tradesCollection.find()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(data.getString("uuidPlayer")));
            try {
                TradeOffline tradeOffline = new TradeOffline(
                        data.get("_id").toString(),
                        player,
                        this.getListItemStackByListDocument((List<Document>) data.get("playerItems")),
                        this.getListItemStackByListDocument((List<Document>) data.get("itemsWant"))
                );
                tradeOffline.setBetHas(data.getDouble("playerBet"));
                tradeOffline.setBetWant(data.getDouble("betWant"));
                response.add(tradeOffline);
            } catch (Exception e) {
                logger.warning("Some problems with trade: " + player.getName() + " " + data.get("_id").toString());
            }

        }
        return response;
    }


    @Override
    public boolean isAvailable() {
        return builder.hasConnected();
    }

    @Override
    public void removeTradeOffline(TradeOffline tradeOffline) {
        tradesCollection.deleteOne(
                eq("_id", new ObjectId(tradeOffline.getUniqueId()))
        );
    }

    /**
     * Convert Array Item Stack to List BSON Documents
     *
     * @param itemStacks Array Item Stacks
     * @return List BSON Documents
     */
    private List<Document> getListDocumentByArrayItemStack(ItemStack[] itemStacks) {
        List<Document> items = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null) {
                items.add(this.convertItemStackToDocument(itemStack));
            }
        }
        return items;
    }


    /**
     * Convert ItemStack to BSON Document
     *
     * @param itemStack item stack
     * @return BSON Document
     */
    private Document convertItemStackToDocument(ItemStack itemStack) {
        Document itemStackDocument = new Document();
        itemStackDocument.append("idName", itemStack.getType().name());
        itemStackDocument.append("amount", itemStack.getAmount());
        List<Document> enchantments = new ArrayList<>();
        itemStack.getEnchantments().forEach((enchantment, integer) -> {
            Document enchantmentDoc = new Document();
            enchantmentDoc.append("name", enchantment.getName());
            enchantmentDoc.append("level", integer);
            enchantments.add(enchantmentDoc);
        });
        itemStackDocument.append("enchantments", enchantments);
        itemStackDocument.append("durability", itemStack.getDurability());
        return itemStackDocument;
    }


    /**
     * Convert List BSON Documents to List ItemStack
     *
     * @param list List BSON Document
     * @return List ItemStack
     * @see ItemStack
     */
    private List<ItemStack> getListItemStackByListDocument(List<Document> list) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (Document document : list) {
            itemStacks.add(this.convertDocumentToItemStack(document));
        }
        return itemStacks;
    }

    /**
     * Convert Document BSON to ItemStack
     *
     * @param document BSON Document
     * @return ItemStack
     * @see ItemStack
     */
    private ItemStack convertDocumentToItemStack(Document document) {
        int durabilityInteger = document.getInteger("durability");
        short durability = (short) durabilityInteger;

        ItemBuilder builder = ItemBuilder.get(Material.getMaterial(document.getString("idName")));

        builder.setAmount(document.getInteger("amount")).setDurability(durability);

        for (Document enchantmentDocument : (List<Document>) document.get("enchantments")) {
            builder.setEnchantment(enchantmentDocument.getString("name"), enchantmentDocument.getInteger("level"), true);
        }

        return builder.build();
    }
}
