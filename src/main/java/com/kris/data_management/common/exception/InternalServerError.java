package com.kris.data_management.common.exception;

public class InternalServerError extends RuntimeException {

    private String description = "";
    public InternalServerError(String message) {
        super(message);
    }

    public InternalServerError(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerError(String message, String description) {
        super(message);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
