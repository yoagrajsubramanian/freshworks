package com.yogaraj.datastore.exception;

public class KeyAlreadyExistsException extends RuntimeException {
    private String message;

    public KeyAlreadyExistsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
