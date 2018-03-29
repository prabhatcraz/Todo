package com.todos.exceptions;

/**
 * To be thrown when a resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}