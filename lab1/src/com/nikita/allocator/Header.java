package com.nikita.allocator;

import com.nikita.util.ArrayUtils;
import com.nikita.util.ByteConverter;

public class Header {
    private int size;
    private int sizePrev;
    private boolean free;

    public Header(int size, int sizePrev) {
        this.size = size;
        this.sizePrev = sizePrev;
        this.free = true;
    }

    public Header(byte[] byteArray) {
        byte[] sizeByteArray = ArrayUtils.splitByteArray(byteArray, 0, 4);
        byte[] sizePrevByteArray = ArrayUtils.splitByteArray(byteArray, 4, 8);
        boolean free = ByteConverter.byteToBoolean(byteArray[8]);

        this.size = ByteConverter.byteArrayToInt(sizeByteArray);
        this.sizePrev = ByteConverter.byteArrayToInt(sizePrevByteArray);
        this.free = free;
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
        byte[] byteArray = new byte[HEADER_SIZE];

        byte[] sizeByteArray = ByteConverter.intToByteArray(size);
        byte[] sizePrevByteArray = ByteConverter.intToByteArray(sizePrev);

        int index = 0;
        index = ArrayUtils.insertByteArray(byteArray, sizeByteArray, index);
        index = ArrayUtils.insertByteArray(byteArray, sizePrevByteArray, index);
        byteArray[index] = ByteConverter.booleanToByte(free);

        return byteArray;
    }

    @Override
    public String toString() {
        return String.format("Size: %d; SizePrev: %d; Free: %b", size, sizePrev, free);
    }

    public final static int HEADER_SIZE = 12;
}
