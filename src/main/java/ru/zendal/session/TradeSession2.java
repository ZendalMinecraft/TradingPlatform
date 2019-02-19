package ru.zendal.session;

import org.bukkit.entity.Player;

/**
 * Model of trade session
 */
public class TradeSession2 {

    /**
     * Is player who create trade
     */
    private final Player seller;

    /**
     * Is player who accept trade
     */
    private final Player buyer;

    /**
     * Bet of seller for trade
     */
    private double betSeller = 0.0;

    /**
     * Bet of buyer for trade
     */
    private double betBuyer = 0.0;

    /**
     * Status trade for seller
     */
    private boolean sellerReady = false;

    /**
     * Status trade for buyer
     */
    private boolean buyerReady = false;


    /**
     * Instantiates a new Trade session 2.
     *
     * @param seller the seller
     * @param buyer  the buyer
     */
    public TradeSession2(Player seller, Player buyer) {
        this.seller = seller;
        this.buyer = buyer;
    }


    /**
     * Gets seller.
     *
     * @return the seller
     */
    public Player getSeller() {
        return seller;
    }

    /**
     * Gets buyer.
     *
     * @return the buyer
     */
    public Player getBuyer() {
        return buyer;
    }

    /**
     * Gets bet seller.
     *
     * @return the bet seller
     */
    public double getBetSeller() {
        return betSeller;
    }

    /**
     * Gets bet buyer.
     *
     * @return the bet buyer
     */
    public double getBetBuyer() {
        return betBuyer;
    }

    /**
     * Sets bet seller.
     *
     * @param betSeller the bet seller
     */
    public void setBetSeller(double betSeller) {
        this.betSeller = betSeller;
    }


    /**
     * Sets bet buyer.
     *
     * @param betBuyer the bet buyer
     */
    public void setBetBuyer(double betBuyer) {
        this.betBuyer = betBuyer;
    }

    /**
     * Is seller ready boolean.
     *
     * @return the boolean
     */
    public boolean isSellerReady() {
        return sellerReady;
    }

    /**
     * Sets seller ready.
     *
     * @param sellerReady the seller ready
     */
    public void setSellerReady(boolean sellerReady) {
        this.sellerReady = sellerReady;
    }

    /**
     * Is buyer ready boolean.
     *
     * @return the boolean
     */
    public boolean isBuyerReady() {
        return buyerReady;
    }

    /**
     * Sets buyer ready.
     *
     * @param buyerReady the buyer ready
     */
    public void setBuyerReady(boolean buyerReady) {
        this.buyerReady = buyerReady;
    }

    /**
     * Is trade ready boolean.
     *
     * @return the boolean
     */
    public boolean isTradeReady() {
        return sellerReady && buyerReady;
    }
}
