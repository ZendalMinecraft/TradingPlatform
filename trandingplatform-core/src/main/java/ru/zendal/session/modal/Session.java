package ru.zendal.session.modal;

import java.util.Collection;

/**
 * Interface of session.
 *
 * @param <T> type of Trader (Player)
 * @param <I> type of item (ItemStack)
 */
public interface Session<T, I> {
    /**
     * Get instance of main trader
     *
     * @return Instance of trader who create trade session
     */
    T getMainTrader();

    /**
     * Get items main trader
     *
     * @return collection of items
     */
    Collection<I> getItemsMainTrader();
}
