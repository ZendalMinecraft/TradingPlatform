/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.core.economy.exception;


/**
 * Economy provider exception
 */
public class EconomyProviderException extends Exception {

    /**
     * Instantiates a new Economy provider exception.
     *
     * @param message the message
     */
    public EconomyProviderException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Economy provider exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public EconomyProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
