package ru.zendal.core.storage.exception;

/**
 * The type Trade session storage exception.
 */
public class TradeSessionStorageException extends RuntimeException {
    /**
     * Instantiates a new Trade session storage exception.
     *
     * @param message the message
     */
    public TradeSessionStorageException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Trade session storage exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public TradeSessionStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
