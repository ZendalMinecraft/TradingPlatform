package ru.zendal.core.session.exception;

/**
 * Throw when trader have't trader
 */
public class EconomyTradeSessionException extends TradeSessionManagerException {
    /**
     * Instantiates a new Economy trade session exception.
     *
     * @param message the message
     */
    public EconomyTradeSessionException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Economy trade session exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public EconomyTradeSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
