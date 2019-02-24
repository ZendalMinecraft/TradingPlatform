package ru.zendal.core.config.parser.exception;

/**
 * Default of exception for parser of trading platform configuration
 */
public class TradingPlatformConfigurationParserException extends RuntimeException {
    /**
     * Instantiates a new Trading platform configuration parser exception.
     *
     * @param message the message
     */
    public TradingPlatformConfigurationParserException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Trading platform configuration parser exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public TradingPlatformConfigurationParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
