package ru.zendal.session.economy;

import org.bukkit.entity.Player;

public interface Economy {


    boolean isEnable();

    boolean hasMoney(Player player, double money);

}
