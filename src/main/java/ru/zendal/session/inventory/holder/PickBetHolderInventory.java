/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.inventory.holder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.zendal.session.Session;
import ru.zendal.session.inventory.PickBetInventoryManager;

public class PickBetHolderInventory implements InventoryHolder, TradingPlatformHolderInventory<PickBetInventoryManager> {


    private final Session session;

    private final PickBetInventoryManager manager;

    public PickBetHolderInventory(PickBetInventoryManager manager, Session session) {
        this.session = session;
        this.manager = manager;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public Inventory getInventory() {
        return manager.getInventory();
    }

    public PickBetInventoryManager getInventoryManager() {
        return manager;
    }
}
