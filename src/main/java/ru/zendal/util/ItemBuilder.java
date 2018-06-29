package ru.zendal.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private ItemStack itemStack;

    private ItemMeta itemMeta;

    private ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }


    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        itemMeta.setDisplayName(displayName);
        return this;
    }

    public ItemBuilder setItemLore(List<String> loreLine) {
        itemMeta.setLore(loreLine);
        return this;
    }

    public ItemBuilder setEnchantment(String nameEnchantment, int level, boolean ignoreLevelRestriction) {
        itemMeta.addEnchant(Enchantment.getByName(nameEnchantment), level, ignoreLevelRestriction);
        return this;
    }

    public ItemBuilder setEnchantment(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }


    public ItemStack build() {
        itemStack.setItemMeta(this.itemMeta);
        return itemStack;
    }

    public static ItemBuilder get(Material material) {
        return new ItemBuilder(material);
    }


}
