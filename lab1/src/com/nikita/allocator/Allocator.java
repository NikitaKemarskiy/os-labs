package com.nikita.allocator;

public class Allocator {
    private int size;
    private byte[] buffer;

    private final static int DEFAULT_SIZE = 1024;

    public Allocator() {
        size = DEFAULT_SIZE;
        buffer = new byte[DEFAULT_SIZE];
    }

    public Allocator(int size) throws InvalidSizeException {
        if (!checkSize(size)) {
            throw new InvalidSizeException();
        }
        this.size = size;
        buffer = new byte[DEFAULT_SIZE];
    }

    public boolean checkSize(int size) {
        return size > Header.HEADER_SIZE && size % 4 != 0;
    }
}