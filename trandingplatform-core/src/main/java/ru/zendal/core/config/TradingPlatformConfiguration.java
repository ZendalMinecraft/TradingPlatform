/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.zendal.core.core.config;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import test.zendal.core.core.config.server.DataBaseServerConfiguration;
import test.zendal.core.core.config.server.SocketServerConfiguration;

@Builder
@Data
@NoArgsConstructor
public final class TradingPlatformConfiguration {

    /**
     * Server side configuration
     */
    private ServerSideConfiguration serverSideConfiguration;

    @Builder
    @Data
    @NoArgsConstructor
    public final class ServerSideConfiguration {

        /**
         * Socket server configuration
         */
        private SocketServerConfiguration socketServerConfiguration;

        /**
         * DataBase server configuration
         */
        private DataBaseServerConfiguration dataBaseServerConfiguration;

    }
}
