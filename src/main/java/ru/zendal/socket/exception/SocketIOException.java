/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.socket.exception;

public class SocketIOException extends Exception {

    public SocketIOException(String message) {
        super(message);
    }

    public SocketIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketIOException(Throwable cause) {
        super(cause);
    }

    public SocketIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SocketIOException() {
    }
}
