package ru.zendal.config;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zendal.config.server.DataBaseServerConfiguration;
import ru.zendal.config.server.SocketServerConfiguration;

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
