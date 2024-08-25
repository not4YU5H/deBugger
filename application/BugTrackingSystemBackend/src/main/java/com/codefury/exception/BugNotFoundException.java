package com.codefury.exception;

public class BugNotFoundException extends Exception{
    public BugNotFoundException() {
        super();
    }

    public BugNotFoundException(String message) {
        super(message);
    }

    public BugNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BugNotFoundException(Throwable cause) {
        super(cause);
    }

    protected BugNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
