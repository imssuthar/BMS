package com.exception;

/**
 * Exception thrown when access is forbidden
 */
public class ForbiddenException extends BMSException {
    public ForbiddenException(String message) {
        super("FORBIDDEN", message, 403);
    }
}

