package com.yogaraj.datastore.exception;

public class InvalidRequestException extends RuntimeException {

    private String message;

    public InvalidRequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
