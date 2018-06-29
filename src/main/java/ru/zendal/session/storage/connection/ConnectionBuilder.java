/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.storage.connection;

public interface ConnectionBuilder<T> {

    /**
     * Get connection
     *
     * @return
     */
    T build();

    /**
     * Has connected
     * @return {@code true} if has connected else {@code false}
     */
    boolean hasConnected();
}
