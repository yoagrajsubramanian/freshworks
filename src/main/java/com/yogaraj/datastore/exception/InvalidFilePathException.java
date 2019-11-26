package com.yogaraj.datastore.exception;

public class InvalidFilePathException extends RuntimeException {

    private String message;

    public InvalidFilePathException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
