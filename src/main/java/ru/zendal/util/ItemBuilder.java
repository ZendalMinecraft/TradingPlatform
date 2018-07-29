/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.zendal.entity.ExtendedItemStack;

import java.util.List;

/**
 * Builder for create ItemStack
 *
 * @see ItemStack
 */
public class ItemBuilder {

    /**
     * Items Stack
     *
     * @see ItemStack
     */
    private ItemStack itemStack;

    /**
     * Item meta
     *
     * @see ItemMeta
     */
    private ItemMeta itemMeta;

    /**
     * Private constructor
     *
     * @param material Material
     * @see Material
     */
    private ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }


    /**
     * Sets amount.
     *
     * @param amount the amount
     * @return the amount
     */
    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Sets durability.
     *
     * @param durability the durability
     * @return the durability
     */
    public ItemBuilder setDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    /**
     * Sets display name.
     *
     * @param displayName the display name
     * @return the display name
     */
    public ItemBuilder setDisplayName(String displayName) {
        itemMeta.setDisplayName(displayName);
        return this;
    }

    /**
     * Sets item lore.
     *
     * @param loreLine the lore line
     * @return the item lore
     */
    public ItemBuilder setItemLore(List<String> loreLine) {
        itemMeta.setLore(loreLine);
        return this;
    }

    /**
     * Sets enchantment.
     *
     * @param nameEnchantment        the name enchantment
     * @param level                  the level
     * @param ignoreLevelRestriction the ignore level restriction
     * @return the enchantment
     */
    public ItemBuilder setEnchantment(String nameEnchantment, int level, boolean ignoreLevelRestriction) {
        itemMeta.addEnchant(Enchantment.getByName(nameEnchantment), level, ignoreLevelRestriction);
        return this;
    }

    /**
     * Sets enchantment.
     *
     * @param enchantment            the enchantment
     * @param level                  the level
     * @param ignoreLevelRestriction the ignore level restriction
     * @return the enchantment
     */
    public ItemBuilder setEnchantment(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }


    /**
     * Build item stack.
     *
     * @return the item stack
     */
    public ItemStack build() {
        itemStack.setItemMeta(this.itemMeta);
        return new ItemStack(itemStack);
    }

    /**
     * Get item builder.
     *
     * @param material the material
     * @return the item builder
     */
    public static ItemBuilder get(Material material) {
        return new ItemBuilder(material);
    }


}
