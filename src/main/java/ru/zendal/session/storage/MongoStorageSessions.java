package ru.zendal.session.storage;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import ru.zendal.session.Session;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.storage.connection.builder.MongoConnectionBuilder;

import java.util.ArrayList;
import java.util.List;

public class MongoStorageSessions implements StorageSessions {


    private final MongoDatabase dataBase;

    public MongoStorageSessions(MongoConnectionBuilder builder) {
        dataBase = builder.build().getDatabase("TradingPlatform");
        this.initCollections();
    }

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
    public String saveSession(Session session) {

        if (session instanceof TradeOfflineSession) {
            return this.processOfflineSession((TradeOfflineSession) session);
        }
        return "";

    }

    private String processOfflineSession(TradeOfflineSession session) {
        MongoCollection<Document> trades = dataBase.getCollection("trades");
        Document data = new Document();
        data.append("uuidPlayer", session.getBuyer().getUniqueId().toString());
        data.append("playerItems", this.getListDocumentByArrayItemStack(session.getSellerItems().toArray(new ItemStack[0])));
        data.append("itemsWant", this.getListDocumentByArrayItemStack(session.getBuyerItems().toArray(new ItemStack[0])));
        trades.insertOne(data);
        Bukkit.broadcastMessage(data.get("_id").toString());
        return data.get("_id").toString();
    }

    @Override
    public List<Session> getAllSessions() {
        return null;
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
}
