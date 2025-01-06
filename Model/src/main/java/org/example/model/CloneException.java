package org.example.model;

public class CloneException extends CloneNotSupportedException {
    public CloneException() {
        super();
    }

    public CloneException(String message) {
        super(message);
    }

    public CloneException(Throwable cause) {
        super(String.valueOf(cause));
    }
}
