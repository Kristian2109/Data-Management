package com.kris.data_management.physical.exception;

public class InvalidSqlIdentifierException extends RuntimeException {

    public InvalidSqlIdentifierException(String identifier) {
        super("The identifier is invalid: " + identifier);
    }

    public InvalidSqlIdentifierException(String identifier, Object details) {
        super("The identifier is invalid: " + identifier + " in " + details.toString());
    }
}
