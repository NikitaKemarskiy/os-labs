package com.nikita.allocator;

import com.nikita.util.ArrayUtils;

public class Block {
    private Header header;
    private byte[] data;

    private static boolean checkSize(int size) {
        return size > 0 && size % 4 == 0;
    }

    public Block(int size, int sizePrev) throws InvalidSizeException {
        if (!checkSize(size)) {
            throw new InvalidSizeException();
        }
        header = new Header(size, sizePrev);
        data = new byte[size];
    }

    public Block(Header header, byte[] data) {
        this.header = header;
        this.data = data;
    }

    public byte[] toByteArray() {
        byte[] headerByteArray = header.toByteArray();
        byte[] byteArray = new byte[headerByteArray.length + data.length];
        ArrayUtils.insertByteArray(byteArray, headerByteArray, 0);
        ArrayUtils.insertByteArray(byteArray, data, headerByteArray.length);
        return byteArray;
    }

    public Header getHeader() {
        return header;
    }
}
