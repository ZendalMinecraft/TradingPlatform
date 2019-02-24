package ru.zendal.core.exception;

/**
 * The type Trading platform launch exception.
 */
public class TradingPlatformLaunchException extends TradingPlatformException {
    /**
     * Instantiates a new Trading platform launch exception.
     *
     * @param message the message
     */
    public TradingPlatformLaunchException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Trading platform launch exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public TradingPlatformLaunchException(String message, Throwable cause) {
        super(message, cause);
    }
}
