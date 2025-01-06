package org.example.model;

public class DbWriteException extends DbException {
    public DbWriteException() {
        super();
    }

    public DbWriteException(String message) {
        super(message);
    }

    public DbWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}