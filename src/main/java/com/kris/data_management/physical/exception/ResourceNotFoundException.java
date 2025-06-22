package com.kris.data_management.physical.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, Object resourceName) {
        super("Resource of type " + resourceName + " with id " + resourceName + " not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
