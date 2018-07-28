package ru.zendal.socket.command.exception;

public class ProcessCommandException extends RuntimeException {

    public ProcessCommandException() {
    }

    public ProcessCommandException(String message) {
        super(message);
    }

    public ProcessCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessCommandException(Throwable cause) {
        super(cause);
    }

    public ProcessCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
