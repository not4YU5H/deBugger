package com.codefury.exception;

public class NoAccessException extends Exception{
    public NoAccessException() {
    }

    public NoAccessException(String message) {
        super(message);
    }

    public NoAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAccessException(Throwable cause) {
        super(cause);
    }

    public NoAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
