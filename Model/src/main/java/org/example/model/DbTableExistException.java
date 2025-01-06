package org.example.model;

public class DbTableExistException extends DbException {
    public DbTableExistException() {
        super();
    }

    public DbTableExistException(String message) {
        super(message);
    }

    public DbTableExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
