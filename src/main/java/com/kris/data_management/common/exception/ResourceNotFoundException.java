package com.kris.data_management.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, Object resourceName) {
        super("Resource of type " + resourceType + " with id " + resourceName + " not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
