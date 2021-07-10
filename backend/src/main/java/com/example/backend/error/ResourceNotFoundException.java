package com.example.backend.error;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message){ super(message);}
}
