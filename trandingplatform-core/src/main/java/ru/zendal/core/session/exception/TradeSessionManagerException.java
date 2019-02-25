package ru.zendal.core.session.exception;

/**
 * The type Trade session manager exception.
 */
public class TradeSessionManagerException extends RuntimeException {
    /**
     * Instantiates a new Trade session manager exception.
     *
     * @param message the message
     */
    public TradeSessionManagerException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Trade session manager exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public TradeSessionManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
