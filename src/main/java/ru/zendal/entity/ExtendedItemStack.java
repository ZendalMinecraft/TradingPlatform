/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.entity;

import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.zendal.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class ExtendedItemStack extends ItemStack {


    public ExtendedItemStack() {

    }

    public ExtendedItemStack(int type) {
        super(type);
    }

    public ExtendedItemStack(Material type) {
        super(type);
    }

    public ExtendedItemStack(int type, int amount) {
        super(type, amount);
    }

    public ExtendedItemStack(Material type, int amount) {
        super(type, amount);
    }

    public ExtendedItemStack(int type, int amount, short damage) {
        super(type, amount, damage);
    }

    public ExtendedItemStack(Material type, int amount, short damage) {
        super(type, amount, damage);
    }

    public ExtendedItemStack(int type, int amount, short damage, Byte data) {
        super(type, amount, damage, data);
    }

    public ExtendedItemStack(Material type, int amount, short damage, Byte data) {
        super(type, amount, damage, data);
    }

    public ExtendedItemStack(ItemStack stack) throws IllegalArgumentException {
        super(stack);
    }

    @Override
    public ExtendedItemStack clone() {
        ExtendedItemStack extendedItemStack = (ExtendedItemStack) super.clone();

        if (this.getItemMeta() != null) {
            extendedItemStack.setItemMeta(getItemMeta().clone());
        }

        if (this.getData() != null) {
            extendedItemStack.setData(this.getData().clone());
        }

        return extendedItemStack;
    }

    public Document toDocument() {
        Document itemStackDocument = new Document();
        itemStackDocument.append("idName", this.getType().name());
        itemStackDocument.append("amount", this.getAmount());
        List<Document> enchantments = new ArrayList<>();
        this.getEnchantments().forEach((enchantment, integer) -> {
            Document enchantmentDoc = new Document();
            enchantmentDoc.append("name", enchantment.getName());
            enchantmentDoc.append("level", integer);
            enchantments.add(enchantmentDoc);
        });
        itemStackDocument.append("enchantments", enchantments);
        itemStackDocument.append("durability", this.getDurability());
        return itemStackDocument;
    }

    public static ExtendedItemStack createByDocument(Document document) {
        int durabilityInteger = document.getInteger("durability");
        short durability = (short) durabilityInteger;

        ItemBuilder builder = ItemBuilder.get(Material.getMaterial(document.getString("idName")));

        builder.setAmount(document.getInteger("amount")).setDurability(durability);

        for (Document enchantmentDocument : (List<Document>) document.get("enchantments")) {
            builder.setEnchantment(enchantmentDocument.getString("name"), enchantmentDocument.getInteger("level"), true);
        }
        return new ExtendedItemStack(builder.build());
    }
}
