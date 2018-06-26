package ru.zendal.session;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * The interface Session.
 */
public interface Session {

    /**
     * Gets seller.
     *
     * @return the seller
     */
    Player getSeller();

    /**
     * Is seller ready.
     *
     * @return the boolean
     */
    boolean isSellerReady();

    /**
     * Sets ready seller.
     *
     * @param status the status
     * @return the ready seller
     */
    TradeSession setReadySeller(boolean status);

    /**
     * Gets buyer.
     *
     * @return the buyer
     */
    Player getBuyer();

    /**
     * Is buyer ready boolean.
     *
     * @return the boolean
     */
    boolean isBuyerReady();

    /**
     * Sets ready buyer.
     *
     * @param status the status
     * @return the ready buyer
     */
    TradeSession setReadyBuyer(boolean status);
}
