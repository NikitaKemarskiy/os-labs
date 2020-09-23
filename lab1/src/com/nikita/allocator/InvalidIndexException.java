package com.nikita.allocator;

public class InvalidIndexException extends Exception {
    public InvalidIndexException() {
        super();
    }

    public InvalidIndexException(String message) {
        super(message);
    }
}
