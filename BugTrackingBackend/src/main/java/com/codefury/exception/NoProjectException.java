package com.codefury.exception;

public class NoProjectException extends Exception{

    public NoProjectException() {
    }

    public NoProjectException(String message) {
        super(message);
    }

    public NoProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoProjectException(Throwable cause) {
        super(cause);
    }

    public NoProjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
