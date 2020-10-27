package com.nikita.allocator;

import com.nikita.util.ArrayUtils;
import com.nikita.util.ByteConverter;

public class BlockHeader {
    private boolean free;

    public BlockHeader() {
        this.free = true;
    }

    public BlockHeader(byte[] byteArray) {
        boolean free = ByteConverter.byteToBoolean(byteArray[0]);
        this.free = free;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public byte[] toByteArray() {
        byte[] byteArray = new byte[BLOCK_HEADER_SIZE];

        int index = 0;
        byteArray[index] = ByteConverter.booleanToByte(free);

        return byteArray;
    }

    @Override
    public String toString() {
        return String.format("Free: %b", free);
    }

    public final static int BLOCK_HEADER_SIZE = 4;
}
