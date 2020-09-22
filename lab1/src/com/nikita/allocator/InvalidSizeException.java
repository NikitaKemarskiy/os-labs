package com.nikita.allocator;

public class InvalidSizeException extends Exception {
    public InvalidSizeException() {
        super();
    }

    public InvalidSizeException(String message) {
        super(message);
    }
}
