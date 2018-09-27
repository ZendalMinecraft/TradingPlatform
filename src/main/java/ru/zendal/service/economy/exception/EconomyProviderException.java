/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.service.economy.exception;

import ru.zendal.service.exception.ServiceException;

/**
 * Economy provider exception
 */
public class EconomyProviderException extends ServiceException {
    public EconomyProviderException(String message) {
        super(message);
    }
}
