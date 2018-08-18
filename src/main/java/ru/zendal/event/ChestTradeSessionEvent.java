/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.event;

import com.google.inject.Inject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import ru.zendal.config.LanguageConfig;
import ru.zendal.config.bundle.InventoryConfigBundle;
import ru.zendal.session.Session;
import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.TradeSession;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.session.exception.TradeSessionManagerException;
import ru.zendal.session.inventory.PickBetInventoryManager;
import ru.zendal.session.inventory.holder.TradeSessionHolderInventory;

/**
 * Event for trading all type Sessions
 */
public class ChestTradeSessionEvent implements Listener {

    /**
     * Session manager
     */
    private final TradeSessionManager manager;
    /**
     *
     */
    private final LanguageConfig languageConfig;


    private final InventoryConfigBundle inventoryConfigBundle;

    @Inject
    public ChestTradeSessionEvent(TradeSessionManager manager, LanguageConfig languageConfig, InventoryConfigBundle inventoryConfigBundle) {
        this.manager = manager;
        this.languageConfig = languageConfig;
        this.inventoryConfigBundle = inventoryConfigBundle;
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
                languageConfig.getMessage("trade.inventory.shift").sendMessage((Player) event.getWhoClicked());
                event.setCancelled(true);
                return;
            }

            if (!event.getClickedInventory().getName().equalsIgnoreCase("container.inventory")) {
                Session session;
                try {
                    session = manager.getSessionByInventory(inventory);
                } catch (TradeSessionManagerException e) {
                    e.printStackTrace();
                    event.setCancelled(true);
                    return;
                }
                if (this.isServiceSlot(event.getSlot())) {
                    event.setCancelled(true);
                    this.changeSessionStatus(event, session);
                    return;
                } else {
                    if (this.canInteractWithSlot(event.getSlot(), (Player) event.getWhoClicked(), session)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
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
                //2 Factor cancel trade
                if (session.getSeller() == event.getWhoClicked()) {
                    if (session.isSellerReady()) {
                        session.setReadySeller(false);
                    } else {
                        this.cancelSession(session);
                    }
                } else if (session.getBuyer() == event.getWhoClicked()) {
                    if (session.isBuyerReady()) {
                        session.setReadyBuyer(false);
                    } else {
                        this.cancelSession(session);
                    }
                }
            } else if (data == 5) {
                if (session.getSeller() == event.getWhoClicked()) {
                    session.setReadySeller(!session.isSellerReady());
                } else if (session.getBuyer() == event.getWhoClicked()) {
                    session.setReadyBuyer(!session.isBuyerReady());
                }

            }
        } else if (event.getCurrentItem().getType() == Material.GOLD_NUGGET && this.isServiceSlot(event.getSlot())) {
            if (session.getSeller() == event.getWhoClicked() && !session.isSellerReady()) {
                this.openInventoryChangeAmount(session, (Player) event.getWhoClicked());
            } else if (session.getBuyer() == event.getWhoClicked() && !session.isBuyerReady()) {
                this.openInventoryChangeAmount(session, (Player) event.getWhoClicked());
            } else {
                languageConfig.getMessage("trade.cant.change.bet.onReady");
            }

        }
    }

    private void cancelSession(Session session) {
        if (session instanceof TradeOfflineSession) {
            manager.cancelOfflineSession((TradeOfflineSession) session);
        } else if (session instanceof TradeSession) {
            manager.cancelSession((TradeSession) session);
        }
    }

    private void openInventoryChangeAmount(Session session, Player whoClicked) {
        whoClicked.openInventory(this.createInventoryForPickBet(session, whoClicked));
    }

    private Inventory createInventoryForPickBet(Session session, Player whoClicked) {
        return new PickBetInventoryManager(session, whoClicked, languageConfig, inventoryConfigBundle.getBetSpread()).getInventory();
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
                    Session session = manager.getSessionByInventory(event.getInventory());
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

    @EventHandler
    public void cancelTradeSessionOnPlayerQuit(PlayerQuitEvent event) {
        manager.getSessionsByPlayer(event.getPlayer()).forEach(manager::cancelSession);
    }

}
