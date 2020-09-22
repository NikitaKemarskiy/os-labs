package com.nikita.allocator;

import com.nikita.util.ArrayUtils;
import com.nikita.util.ByteConverter;

public class Header {
    private int size;
    private int sizePrev;
    private boolean free;

    public Header(int size) {
        this.size = size;
        this.free = true;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSizePrev() {
        return sizePrev;
    }

    public void setSizePrev(int sizePrev) {
        this.sizePrev = sizePrev;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public byte[] toByteArray() {
        byte[] byteArray = new byte[12];

        byte[] sizeByteArray = ByteConverter.intToByteArray(size);
        byte[] sizePrevByteArray = ByteConverter.intToByteArray(sizePrev);

        int index = 0;
        index = ArrayUtils.insertByteArray(byteArray, sizeByteArray, index);
        index = ArrayUtils.insertByteArray(byteArray, sizePrevByteArray, index);
        byteArray[index] = ByteConverter.booleanToByte(free);

        return byteArray;
    }

    public final static int HEADER_SIZE = 12;
}
