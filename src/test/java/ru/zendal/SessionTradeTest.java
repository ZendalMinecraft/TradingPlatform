/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.zendal.session.exception.TradeSessionManagerException;
import ru.zendal.session.inventory.holder.TradeSessionHolderInventory;

import static org.junit.Assert.fail;

public class SessionTradeTest {
    private ServerMock server;
    private TradingPlatform plugin;

    private PlayerMock player1;
    private PlayerMock player2;

    @Before
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(TradingPlatform.class);
        player1 = server.addPlayer();
        player2 = server.addPlayer();

        ((Player)player1).setDisplayName("p1");
        ((Player)player2).setDisplayName("p2");
    }

    @After
    public void tearDown() {
        MockBukkit.unload();
    }


    @Test
    public void test() throws Exception {
        plugin.getTradeSessionManager().createSession(player1, player2);
        plugin.getTradeSessionManager().getSessionForSellerAndBuyer(player1, player2);
        plugin.getTradeSessionManager().getSessionByBuyer(player2);
        server.execute("trade",player1,"to p2");
        server.execute("trade",player2,"confirm");
        if (!(player2.getOpenInventory().getTopInventory().getHolder() instanceof TradeSessionHolderInventory)){
            fail();
        }
        Assert.assertEquals(player2.getOpenInventory().getTopInventory().getTitle(),"p1(×)                     p2(×)");

        InventoryClickEvent event = new InventoryClickEvent(player2.getOpenInventory(),
                InventoryType.SlotType.CONTAINER,
                13,
                ClickType.LEFT,
                InventoryAction.PICKUP_SOME);
        Bukkit.getPluginManager().callEvent(event);
        System.out.println(event.getCurrentItem());
    }

    @Test(expected = TradeSessionManagerException.class)
    public void testUndefinedSession() throws TradeSessionManagerException {
    }
}
