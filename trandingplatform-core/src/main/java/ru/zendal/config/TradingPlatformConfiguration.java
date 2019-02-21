package ru.zendal.config;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zendal.config.server.SocketServerConfiguration;

@Builder
@Data
@NoArgsConstructor
public final class TradingPlatformConfiguration {


    private ServerSideConfiguration serverSideConfiguration;


    @Builder
    @Data
    @NoArgsConstructor
    public final class ServerSideConfiguration {

        private SocketServerConfiguration socketServerConfiguration;


    }
}
