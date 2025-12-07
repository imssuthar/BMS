package com.bms.common.exception;

/**
 * Exception thrown when a requested resource is not found
 */
public class ResourceNotFoundException extends BMSException {
    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message, 404);
    }

    public ResourceNotFoundException(String resource, String identifier) {
        super("RESOURCE_NOT_FOUND", 
              String.format("%s with identifier '%s' not found", resource, identifier), 
              404);
    }
}

