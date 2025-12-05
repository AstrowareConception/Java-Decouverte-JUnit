package com.example.bank;

/**
 * Exception levée en cas de découvert non autorisé.
 */
public class OverdraftException extends RuntimeException {
    public OverdraftException(String message) {
        super(message);
    }
}
