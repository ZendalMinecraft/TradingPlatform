package ru.zendal.session;

import org.bukkit.entity.Player;

/**
 * The interface Session.
 */
public interface Session {

    public static int INDEX_SLOT_ECONOMY_CONTROL = 22;

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
