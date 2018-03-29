package com.todos.exceptions;

/**
 * To be thrown when a Todo of same symbol to be created already exists.
 */
public class StockAlreadyExistsException extends RuntimeException {
    public StockAlreadyExistsException(final String message) {
        super(message);
    }
}