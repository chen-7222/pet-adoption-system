package com.petadoption.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) { // 這個建構子很重要！
        super(message);
    }
}