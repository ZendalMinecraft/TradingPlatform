/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config.bundle.builder.exception;

public class FieldNotInitializedException extends RuntimeException {

    public FieldNotInitializedException(String message) {
        super(message);
    }
}
