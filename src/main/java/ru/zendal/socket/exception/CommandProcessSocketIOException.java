/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.socket.exception;

public class CommandProcessSocketIOException extends SocketIOException {


    public static final int CODE_UNDEFINED_COMMAND = 1;
    public static final int CODE_MISSING_REQUIRED_KEY = 81;

    /**
     * Error code
     */
    private final int codeError;

    public CommandProcessSocketIOException(String errorMessage, int codeError) {
        super(errorMessage);
        this.codeError = codeError;
    }

    /**
     * Get error code
     *
     * @return error code
     */
    public int getErrorCode() {
        return this.codeError;
    }
}
