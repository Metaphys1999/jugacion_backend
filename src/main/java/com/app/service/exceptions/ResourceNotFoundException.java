package com.app.service.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Recurso no encontrado en el servidor");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
