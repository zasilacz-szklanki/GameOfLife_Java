package org.example.model;

public class DoStepException extends Exception {
    public DoStepException() {
        super();
    }

    public DoStepException(String message) {
        super(message);
    }

    public DoStepException(String message, Throwable cause) {
        super(message, cause);
    }
}
