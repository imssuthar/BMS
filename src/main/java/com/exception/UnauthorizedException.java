package com.exception;

/**
 * Exception thrown when authentication/authorization fails
 */
public class UnauthorizedException extends BMSException {
    public UnauthorizedException(String message) {
        super("UNAUTHORIZED", message, 401);
    }
}

