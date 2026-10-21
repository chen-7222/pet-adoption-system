package com.petadoption.exception;

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException() {
        super();
    }

    public InvalidStatusException(String message) {
        super(message);
    }
}