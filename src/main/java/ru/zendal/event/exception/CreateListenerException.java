/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.event.exception;

/**
 * On failed create listener
 */
public class CreateListenerException extends RuntimeException {

    public CreateListenerException() {
    }

    public CreateListenerException(String message) {
        super(message);
    }

    public CreateListenerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateListenerException(Throwable cause) {
        super(cause);
    }

    public CreateListenerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
