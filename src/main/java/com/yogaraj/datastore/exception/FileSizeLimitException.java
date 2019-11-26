package com.yogaraj.datastore.exception;

public class FileSizeLimitException extends RuntimeException {
    private String message;

    public FileSizeLimitException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
