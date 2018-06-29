package ru.zendal.session;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.session.inventory.ViewOfflineTradeHolderInventory;
import ru.zendal.util.ItemBuilder;

import java.util.List;

public class TradeOffline {


    private final Player player;

    private final List<ItemStack> has;
    private final List<ItemStack> wants;
    private final String uniqueId;

    private Inventory inventory;

    public TradeOffline(String uniqueId,Player player, List<ItemStack> has, List<ItemStack> wants) {
        this.uniqueId =uniqueId;
        this.player = player;
        this.has = has;
        this.wants = wants;
    }

    public Player getPlayer() {
        return player;
    }

    public List<ItemStack> getHas() {
        return has;
    }

    public List<ItemStack> getWants() {
        return wants;
    }


    public Inventory getInventory() {
        if (inventory == null) {
            inventory = this.generateInventory();
        }
        return inventory;
    }

    private Inventory generateInventory() {
        Inventory inventory = Bukkit.createInventory(new ViewOfflineTradeHolderInventory(), 54);
        this.addedItemsToInventory(inventory);
        int indexSlot = 4;
        ItemStack stick = ItemBuilder.get(Material.STICK).build();
        for (int indexLine = 0; indexLine < 6; indexLine++) {
            inventory.setItem(indexSlot, stick);
            indexSlot += 9;
        }
        for (indexSlot = 45; indexSlot < 54; indexSlot++) {
            inventory.setItem(indexSlot, stick);
        }
        inventory.setItem(9 * 1 + 4, ItemBuilder.get(Material.WOOL).setDurability((short) 5).build());
        inventory.setItem(9 * 4 + 4, ItemBuilder.get(Material.WOOL).setDurability((short) 14).build());

        return inventory;
    }

    private void addedItemsToInventory(Inventory inventory) {
        int indexSlot = 0;
        for (ItemStack itemStack : has) {
            inventory.setItem(indexSlot, itemStack);
            if ((indexSlot + 6) % 9 == 0) {
                indexSlot += 5;
            }
            indexSlot++;
        }
        indexSlot = 5;
        for (ItemStack itemStack : wants) {
            inventory.setItem(indexSlot, itemStack);
            if ((indexSlot + 1) % 9 == 0) {
                indexSlot += 5;
            }
            indexSlot++;
        }
    }

    public static TradeOffline factory(String uniqueId,TradeOfflineSession session) {
        return new TradeOffline(uniqueId,session.getBuyer(), session.getSellerItems(), session.getBuyerItems());
    }

    public String getUniqueId() {
        return uniqueId;
    }
}
