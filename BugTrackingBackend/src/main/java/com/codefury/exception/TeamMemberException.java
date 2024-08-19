package com.codefury.exception;

public class TeamMemberException extends Exception{
    public TeamMemberException() {
    }

    public TeamMemberException(String message) {
        super(message);
    }

    public TeamMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeamMemberException(Throwable cause) {
        super(cause);
    }

    public TeamMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
