package com.yogaraj.datastore.exception;

public class KeyExpiredException extends RuntimeException {

    private String message;

    public KeyExpiredException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
