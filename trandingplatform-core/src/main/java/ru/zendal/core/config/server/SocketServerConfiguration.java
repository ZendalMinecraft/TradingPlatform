/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.core.config.server;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class SocketServerConfiguration {

    private boolean enable;

    private String host;

    private Integer port;
}
