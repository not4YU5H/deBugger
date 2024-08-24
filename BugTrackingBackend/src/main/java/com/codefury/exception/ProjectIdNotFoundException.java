package com.codefury.exception;

public class ProjectIdNotFoundException extends Exception{
    public ProjectIdNotFoundException() {
    }

    public ProjectIdNotFoundException(String message) {
        super(message);
    }

    public ProjectIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectIdNotFoundException(Throwable cause) {
        super(cause);
    }

    public ProjectIdNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
