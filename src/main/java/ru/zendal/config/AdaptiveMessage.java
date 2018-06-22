package ru.zendal.config;

import org.bukkit.entity.Player;

public class AdaptiveMessage {

    private String message;

    AdaptiveMessage(String message) {
        this.message = message;
    }


    public AdaptiveMessage setSeller(Player player) {
        this.message = this.message.replaceAll("%seller%", player.getDisplayName());
        return this;
    }


    public AdaptiveMessage setBuyer(Player player) {
        this.message = this.message.replaceAll("%buyer%", player.getDisplayName());
        return this;
    }

    public AdaptiveMessage setCustomMessage(int index, String text) {
        this.message = this.message.replace("%" + index + "%", text);
        return this;
    }

    public void sendMessage(Player player) {
        for (String text : this.message.split("\n")) {
            player.sendMessage(text);
        }
    }


    @Override
    public String toString() {
        return this.message;
    }
}
