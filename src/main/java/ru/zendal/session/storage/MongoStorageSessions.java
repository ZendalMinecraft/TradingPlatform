package ru.zendal.session.storage;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.TradeSession;

import java.util.ArrayList;
import java.util.List;

//TODO Create MongoConnection
public class MongoStorageSessions implements StorageSessions {


    private final MongoDatabase dataBase;

    public MongoStorageSessions() {
        MongoClient mongoClient = MongoClients.create();
        dataBase = mongoClient.getDatabase("TradingPlatform");
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
    public void saveSession(TradeSession session) {

    }

    @Override
    public void saveOfflineSession(TradeOfflineSession session) {
        MongoCollection<Document> trades = dataBase.getCollection("trades");
        Document data = new Document();
        data.append("id", 1);
        data.append("uuidPlayer", session.getPlayer().getUniqueId().toString());
        List<Document> items = new ArrayList<>();
        for (ItemStack item : session.getInventory().getContents()) {
            if (item == null) {
                continue;
            }
            Document itemDoc = new Document();
            itemDoc.append("material", item.getType().toString());
            itemDoc.append("name", item.getItemMeta().getDisplayName());
            items.add(itemDoc);
        }
        data.append("items", items);
        trades.insertOne(data);
        Bukkit.broadcastMessage("Et");
    }

    @Override
    public List<TradeSession> getAllSessions() {
        return null;
    }

    @Override
    public List<TradeOfflineSession> getAllOfflineSessions() {
        return null;
    }
}
