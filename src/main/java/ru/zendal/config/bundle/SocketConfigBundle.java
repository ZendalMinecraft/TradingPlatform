/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config.bundle;

/**
 * Bundle configuration data for prepare Socket Server
 */
public class SocketConfigBundle {

    /**
     * Charset incoming message
     */
    private String charset = "UTF-8";

    /**
     * Listener port
     */
    private int port = 2424;

    /**
     * Setup listener port
     *
     * @param port listener port
     * @return bundle
     */
    public SocketConfigBundle setPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * Setup charset incoming message
     *
     * @param charset the charset
     * @return charset
     */
    public SocketConfigBundle setCharset(String charset) {
        this.charset = charset;
        return this;
    }


    /**
     * Get listener port
     *
     * @return listener port
     */
    public int getPort() {
        return port;
    }

    /**
     * Get incoming message charset
     *
     * @return incoming message charset
     */
    public String getCharset() {
        return charset;
    }


    @Override
    public String toString() {
        return "{Port:" + port + ", Charset:" + charset + "}";
    }
}
