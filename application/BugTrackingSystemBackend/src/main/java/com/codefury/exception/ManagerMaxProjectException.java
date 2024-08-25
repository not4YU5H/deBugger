package com.codefury.exception;

public class ManagerMaxProjectException extends Exception{
    public ManagerMaxProjectException() {
    }

    public ManagerMaxProjectException(String message) {
        super(message);
    }

    public ManagerMaxProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerMaxProjectException(Throwable cause) {
        super(cause);
    }

    public ManagerMaxProjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
