/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class MetaDataMessage {


    private final AdaptiveMessage message;
    private ClickEvent clickEvent;
    private HoverEvent hoverEvent;


    private MetaDataMessage(AdaptiveMessage message) {
        this.message = message;
    }


    public MetaDataMessage setClickEvent(ClickEvent.Action action, String value) {
        this.clickEvent = new ClickEvent(action, value);
        return this;
    }


    public MetaDataMessage addHoverText(String text) {
        this.hoverEvent = new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new BaseComponent[]{new TextComponent(text)}
        );
        return this;
    }


    ClickEvent getClickEvent() {
        return clickEvent;
    }

    HoverEvent getHoverEvent() {
        return hoverEvent;
    }

    public void sendMessage(Player player) {
        message.sendMessage(player);
    }

    static MetaDataMessage getInstance(AdaptiveMessage message) {
        return new MetaDataMessage(message);
    }

}
