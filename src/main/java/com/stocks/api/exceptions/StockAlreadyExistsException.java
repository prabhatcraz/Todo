package com.stocks.api.exceptions;

public class StockAlreadyExistsException extends RuntimeException {
    public StockAlreadyExistsException(final String message) {
        super(message);
    }
}