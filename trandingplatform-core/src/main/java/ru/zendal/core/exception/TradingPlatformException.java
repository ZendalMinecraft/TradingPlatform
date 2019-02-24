package ru.zendal.core.exception;

/**
 * The type Trading platform exception.
 */
public class TradingPlatformException extends RuntimeException {

    /**
     * Instantiates a new Trading platform exception.
     *
     * @param message the message
     */
    public TradingPlatformException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Trading platform exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public TradingPlatformException(String message, Throwable cause) {
        super(message, cause);
    }
}
