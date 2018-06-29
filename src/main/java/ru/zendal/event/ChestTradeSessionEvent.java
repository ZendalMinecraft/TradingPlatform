package ru.zendal.event;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.zendal.TradingPlatform;
import ru.zendal.session.Session;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.TradeSession;
import ru.zendal.session.exception.TradeSessionManagerException;
import ru.zendal.session.inventory.ControlEconomyHolderInventory;
import ru.zendal.session.inventory.TradeSessionHolderInventory;
import ru.zendal.util.ItemBuilder;

public class ChestTradeSessionEvent implements Listener {


    private final TradingPlatform plugin;

    public ChestTradeSessionEvent(TradingPlatform instance) {
        this.plugin = instance;
    }


    @EventHandler
    public void onChestMoveItem(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof TradeSessionHolderInventory) {
            if (event.getClickedInventory() == null) {
                return;
            }
            //TODO added method for Detect changed Inventory
            if (event.isShiftClick()) {
                plugin.getAdaptiveMessage("trade.inventory.shift").sendMessage((Player) event.getWhoClicked());
                event.setCancelled(true);
                return;
            }
            if (!event.getClickedInventory().getName().equalsIgnoreCase("container.inventory")) {
                Session session;
                try {
                    session = plugin.getSessionManager().getSessionByInventory(inventory);
                } catch (TradeSessionManagerException e) {
                    e.printStackTrace();
                    event.setCancelled(true);
                    return;
                }
                if (event.getSlot() == Session.INDEX_SLOT_ECONOMY_CONTROL) {
                    event.setCancelled(true);
                    this.openEconomyControllEvent((Player) event.getWhoClicked(), session);
                } else if (this.isServiceSlot(event.getSlot())) {
                    event.setCancelled(true);
                    this.changeSessionStatus(event, session);
                } else {
                    if (this.canInteractWithSlot(event.getSlot(), (Player) event.getWhoClicked(), session)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }


    private void openEconomyControllEvent(Player player, Session session) {
        Inventory inventory = Bukkit.createInventory(new ControlEconomyHolderInventory(session), 9 * 4, "EconomyConrtoll");
        ItemStack glass = ItemBuilder.get(Material.STAINED_GLASS_PANE).setDurability((short) 15).build();
        for (int indexSlot = 0; indexSlot < inventory.getSize(); indexSlot++) {
            inventory.setItem(indexSlot, glass);
        }


        //TODO Optimize
        ItemBuilder goldNugget = ItemBuilder.get(Material.GOLD_NUGGET);
        ItemBuilder ironNugget = ItemBuilder.get(Material.IRON_NUGGET);

        inventory.setItem(11, goldNugget.setDisplayName("+1").build());
        inventory.setItem(12, goldNugget.setDisplayName("+5").build());
        inventory.setItem(13, goldNugget.setDisplayName("+10").build());
        inventory.setItem(14, goldNugget.setDisplayName("+100").build());
        inventory.setItem(15, goldNugget.setDisplayName("+1000").build());


        inventory.setItem(20, ironNugget.setDisplayName("-1").build());
        inventory.setItem(21, ironNugget.setDisplayName("-5").build());
        inventory.setItem(22, ironNugget.setDisplayName("-10").build());
        inventory.setItem(23, ironNugget.setDisplayName("-100").build());
        inventory.setItem(24, ironNugget.setDisplayName("-1000").build());

        player.openInventory(inventory);
    }

    /**
     * Change player trade status
     *
     * @param event   Event click on Inventory
     * @param session Trade Session
     * @see InventoryClickEvent
     * @see TradeSession
     */
    private void changeSessionStatus(InventoryClickEvent event, Session session) {
        if (event.getCurrentItem().getType() == Material.WOOL) {
            byte data = event.getCurrentItem().getData().getData();
            if (data == 14) {
                this.cancelSession(session);
            } else if (data == 5) {
                if (session.getSeller() == event.getWhoClicked()) {
                    session.setReadySeller(!session.isSellerReady());
                } else if (session.getBuyer() == event.getWhoClicked()) {
                    session.setReadyBuyer(!session.isBuyerReady());
                }

            }
        }
    }

    private void cancelSession(Session session) {
        if (session instanceof TradeOfflineSession) {
            this.plugin.getSessionManager().cancelOfflineSession((TradeOfflineSession) session);
        } else if (session instanceof TradeSession) {
            this.plugin.getSessionManager().cancelSession((TradeSession) session);
        }
    }

    /**
     * On player drag item into TradeSessionHolderInventory
     *
     * @param event Event Inventory Drag Event
     * @see TradeSessionHolderInventory
     * @see InventoryDragEvent
     */
    @EventHandler
    public void onDragEventInTrade(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof TradeSessionHolderInventory) {
            event.getRawSlots().forEach(data -> {
                if (data > 53) {
                    return;
                }
                try {
                    Session session = plugin.getSessionManager().getSessionByInventory(event.getInventory());
                    if (this.canInteractWithSlot(data, (Player) event.getWhoClicked(), session)) {
                        event.setCancelled(true);
                    }
                } catch (TradeSessionManagerException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private boolean canInteractWithSlot(int indexSlot, Player whoClicked, Session session) {
        //Check foreign slot (Buyer)
        if (isSellerSlot(indexSlot) && session.getSeller() != whoClicked) {
            return true;
        }
        //Check foreign slot (Seller)
        if (!isSellerSlot(indexSlot) && session.getSeller() == whoClicked) {
            return true;
        }

        //Check Seller is ready
        if (session.getSeller() == whoClicked && session.isSellerReady()) {
            return true;
        }

        //Check Buyer is ready
        if (session.getBuyer() == whoClicked && session.isBuyerReady()) {
            return true;
        }
        return false;
    }

    /**
     * Check is Service slot
     *
     * @param slot index slot
     * @return {@code true} if slot is service else {@code false}
     */
    private boolean isServiceSlot(int slot) {
        if ((slot + 5) % 9 == 0) {
            return true;
        }
        return slot >= 45;
    }

    /**
     * Check is slot Seller
     *
     * @param slot index slot
     * @return {@code true} if slot is seller slot else {@code false}
     */
    private boolean isSellerSlot(int slot) {
        for (int index = 0; index < 5; index++) {
            if ((slot + 5) % 9 == 0) {
                return true;
            }
            slot++;
        }
        return false;
    }
}
