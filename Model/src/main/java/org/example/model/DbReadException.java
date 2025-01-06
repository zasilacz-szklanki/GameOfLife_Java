package org.example.model;

public class DbReadException extends DbException {
    public DbReadException() {
        super();
    }

    public DbReadException(String message) {
        super(message);
    }

    public DbReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
