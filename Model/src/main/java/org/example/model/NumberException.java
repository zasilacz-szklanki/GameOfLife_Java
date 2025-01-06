package org.example.model;

public class NumberException extends NumberFormatException {
    public NumberException() {
        super();
    }

    public NumberException(String message) {
        super(message);
    }
}
