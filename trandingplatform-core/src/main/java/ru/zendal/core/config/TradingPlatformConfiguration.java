/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.core.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.zendal.core.config.server.DataBaseServerConfiguration;
import ru.zendal.core.config.server.SocketServerConfiguration;

@Builder
@Data
public final class TradingPlatformConfiguration {

    /**
     * Version of configuration structure
     */
    private Long version;

    /**
     * Server side configuration
     */
    private ServerSideConfiguration serverSideConfiguration;


    @Data
    @Builder
    @AllArgsConstructor
    public static class ServerSideConfiguration {

        /**
         * Socket server configuration
         */
        private SocketServerConfiguration socket;

        /**
         * DataBase server configuration
         */
        private DataBaseServerConfiguration dataSource;

    }
}
