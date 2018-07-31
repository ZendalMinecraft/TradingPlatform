/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.event.exception;

public class EventRegisterException extends RuntimeException {
    public EventRegisterException() {
    }

    public EventRegisterException(String message) {
        super(message);
    }

    public EventRegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventRegisterException(Throwable cause) {
        super(cause);
    }

    public EventRegisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
