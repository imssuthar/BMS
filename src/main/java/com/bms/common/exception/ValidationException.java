package com.bms.common.exception;

/**
 * Exception thrown when validation fails
 */
public class ValidationException extends BMSException {
    public ValidationException(String message) {
        super("VALIDATION_ERROR", message, 400);
    }

    public ValidationException(String field, String message) {
        super("VALIDATION_ERROR", 
              String.format("Validation failed for field '%s': %s", field, message), 
              400);
    }
}

