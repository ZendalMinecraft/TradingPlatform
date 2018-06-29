package ru.zendal.session.storage;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.zendal.session.Session;
import ru.zendal.session.TradeOffline;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.storage.connection.builder.MongoConnectionBuilder;
import ru.zendal.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class MongoStorageSessions implements StorageSessions {


    private final MongoDatabase dataBase;
    private final MongoConnectionBuilder builder;
    private final Logger logger;


    public MongoStorageSessions(MongoConnectionBuilder builder, Logger logger) {
        this.builder = builder;
        this.logger = logger;
        dataBase = builder.build();
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
        return data.get("_id").toString();
    }

    @Override
    public List<TradeOffline> getAllSessions() {
        List<TradeOffline> response = new ArrayList<>();
        for (Document data : dataBase.getCollection("trades").find()) {
            response.add(new TradeOffline(
                    data.get("_id").toString(),
                    (Player) Bukkit.getOfflinePlayer(UUID.fromString(data.getString("uuidPlayer"))),
                    this.getListItemStackByListDocument((List<Document>) data.get("playerItems")),
                    this.getListItemStackByListDocument((List<Document>) data.get("itemsWant"))
            ));

        }
        return response;
    }


    @Override
    public boolean isAvailable() {
        return builder.hasConnected();
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


    private List<ItemStack> getListItemStackByListDocument(List<Document> list) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (Document document : list) {
            itemStacks.add(this.convertDocumentToItemStack(document));
        }
        return itemStacks;
    }

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
