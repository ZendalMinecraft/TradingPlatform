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
     * Listener host
     */
    private String host = "localhost";
    /**
     * Charset incoming message
     */
    private String charset = "UTF-8";

    /**
     * Listener port
     */
    private int port = 2424;

    /**
     * Setup listener host
     * @param host listener host
     * @return bundle
     */
    public SocketConfigBundle setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * Setup listener port
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
     * Get listener host
     * @return listener host
     */
    public String getHost() {
        return host;
    }

    /**
     * Get listener port
     * @return listener port
     */
    public int getPort() {
        return port;
    }

    /**
     * Get incoming message charset
     * @return incoming message charset
     */
    public String getCharset() {
        return charset;
    }
}
