package ru.zendal.core.storage.exception;

/**
 * The type Save trade session storage exception.
 */
public class SaveTradeSessionStorageException extends TradeSessionStorageException {
    /**
     * Instantiates a new Save trade session storage exception.
     *
     * @param message the message
     */
    public SaveTradeSessionStorageException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Save trade session storage exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public SaveTradeSessionStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
