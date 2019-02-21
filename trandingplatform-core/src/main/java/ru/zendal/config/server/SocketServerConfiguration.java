package ru.zendal.config.server;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public final class SocketServerConfiguration {

    private boolean enable;

    private String host;

    private Integer port;
}
