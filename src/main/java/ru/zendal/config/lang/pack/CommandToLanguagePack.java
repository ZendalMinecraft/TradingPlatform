package ru.zendal.config.lang.pack;

import org.bukkit.entity.Player;

public interface CommandToLanguagePack {

    /**
     * Send message on request
     *
     * @param buyer instance buyer (Player)
     */
    void sendMessageOnRequest(Player buyer);

    /**
     * getMessageOnSecondPlayerConfirmTrade
     *
     * @param nameOfBuyer name of Buyer
     * @return message on second player confirm trade
     */
    String getMessageOnSecondPlayerConfirmTrade(String nameOfBuyer);

}
