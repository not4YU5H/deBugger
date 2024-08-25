package com.codefury.exception;

// Custom exception to handle cases where a user is not assigned to any projects
public class NoProjectsAssignedException extends Exception {

    // Constructor that accepts a custom error message
    public NoProjectsAssignedException(String message) {
        super(message);
    }

    // Optional: Additional constructor to include the cause of the exception
    public NoProjectsAssignedException(String message, Throwable cause) {
        super(message, cause);
    }
}
