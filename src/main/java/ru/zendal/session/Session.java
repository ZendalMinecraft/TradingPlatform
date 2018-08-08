/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

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
    Session setReadySeller(boolean status);

    /**
     * Get bet Seller
     *
     * @return bet amount
     */
    double getBetSeller();

    /**
     * Set value bet Seller
     *
     * @param bet value bet
     * @return Session
     */
    Session setBetSeller(double bet);


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
    Session setReadyBuyer(boolean status);

    /**
     * Get bet Buyer
     *
     * @return bet amount
     */
    double getBetBuyer();

    /**
     * Set value bet Buyer
     *
     * @param bet value bet
     * @return Session
     */
    Session setBetBuyer(double bet);

    /**
     * Get inventory for trading
     *
     * @return CraftInventory
     */
    Inventory getInventory();
}
