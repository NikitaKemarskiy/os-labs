package com.nikita.allocator;

import com.nikita.util.ArrayUtils;

public class Block {
    private Header header;
    private byte[] data;

    public Block(int size) throws InvalidSizeException {
        if (size <= 0) {
            throw new InvalidSizeException();
        }
        header = new Header(size);
        data = new byte[size];
    }

    public byte[] toByteArray() {
        byte[] headerByteArray = header.toByteArray();
        byte[] byteArray = new byte[headerByteArray.length + data.length];
        ArrayUtils.insertByteArray(byteArray, data, headerByteArray.length);
        return byteArray;
    }

    public boolean checkSize(int size) {
        return size > Header.HEADER_SIZE && size % 4 != 0;
    }

    public Header getHeader() {
        return header;
    }
}
