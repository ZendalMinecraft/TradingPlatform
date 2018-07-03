/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config.bundle;

import io.scalecube.socketio.SocketIOServer;

public class SocketConfigBundle {
    private String host = "localhost";
    private int port = 2424;

    public SocketConfigBundle(){

    }

    public SocketConfigBundle setHost(String host) {
        this.host = host;
        return this;
    }

    public SocketConfigBundle setPort(int port) {
        this.port = port;
        return this;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
