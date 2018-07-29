/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * Adaptive message for any players
 */
public class AdaptiveMessage {

    private final MetaDataMessage metaData;
    private String message;

    AdaptiveMessage(String message) {
        this.message = message;
        this.metaData = MetaDataMessage.getInstance(this);
    }

    /**
     * Replace seller holder to name Seller
     *
     * @param player Instance Player
     * @return self
     */
    public AdaptiveMessage setSeller(Player player) {
        this.message = this.message.replaceAll("%seller%", player.getDisplayName());
        return this;
    }

    /**
     * Replace buyer holder to name Seller
     *
     * @param player Instance PlayerInstance Player
     * @return self
     */
    public AdaptiveMessage setBuyer(Player player) {
        this.message = this.message.replaceAll("%buyer%", player.getDisplayName());
        return this;
    }

    /**
     * Set custom message at index
     *
     * @param index index place
     * @param text  custom text
     * @return self
     */
    public AdaptiveMessage setCustomMessage(int index, String text) {
        this.message = this.message.replace("%" + index + "%", text);
        return this;
    }

    public MetaDataMessage getMetaData() {
        return metaData;
    }

    public void sendMessage(Player player) {
        for (String text : this.message.split("\n")) {
            TextComponent textComponent = new TextComponent(text);
            textComponent.setHoverEvent(metaData.getHoverEvent());
            textComponent.setClickEvent(metaData.getClickEvent());
            player.spigot().sendMessage(textComponent);
        }
    }


    @Override
    public String toString() {
        return this.message;
    }
}
