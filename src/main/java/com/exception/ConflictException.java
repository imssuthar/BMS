package com.exception;

/**
 * Exception thrown when a resource conflict occurs (e.g., duplicate email)
 */
public class ConflictException extends BMSException {
    public ConflictException(String message) {
        super("CONFLICT", message, 409);
    }

    public ConflictException(String resource, String identifier) {
        super("CONFLICT", 
              String.format("%s with identifier '%s' already exists", resource, identifier), 
              409);
    }
}

