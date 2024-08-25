package com.codefury.exception;

public class ProjectStartDateException extends Exception{
    public ProjectStartDateException() {
    }

    public ProjectStartDateException(String message) {
        super(message);
    }

    public ProjectStartDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectStartDateException(Throwable cause) {
        super(cause);
    }

    public ProjectStartDateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
