package com.nikita.allocator;

import com.nikita.util.ArrayUtils;
import com.nikita.util.ByteConverter;

public class PageHeader {
    private boolean free;
    private PageType pageType;
    private int blockNumberOfPages;
    private int blockPageIndex;

    public PageHeader() {
        free = true;
        pageType = PageType.EMPTY;
    }

    public PageHeader(byte[] byteArray) {
        boolean free = ByteConverter.byteToBoolean(byteArray[0]);

        byte[] blockTypeByteArray = ArrayUtils.splitByteArray(byteArray, 4, 8);
        byte[] blockNumberOfPagesByteArray = ArrayUtils.splitByteArray(byteArray, 8, 12);
        byte[] blockPageIndexByteArray = ArrayUtils.splitByteArray(byteArray, 12, 16);

        this.free = free;
        this.pageType = PageType.valueOf(ByteConverter.byteArrayToInt(blockTypeByteArray));
        this.blockNumberOfPages = ByteConverter.byteArrayToInt(blockNumberOfPagesByteArray);
        this.blockPageIndex = ByteConverter.byteArrayToInt(blockPageIndexByteArray);
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public byte[] toByteArray() {
        byte[] byteArray = new byte[PAGE_HEADER_SIZE];

        byte[] blockTypeArray = ByteConverter.intToByteArray(pageType.getSize());
        byte[] blockNumberOfPagesArray = ByteConverter.intToByteArray(blockNumberOfPages);
        byte[] blockPageIndexArray = ByteConverter.intToByteArray(blockPageIndex);

        byteArray[0] = ByteConverter.booleanToByte(free);
        int index = 4;

        index = ArrayUtils.insertByteArray(byteArray, blockTypeArray, index);
        index = ArrayUtils.insertByteArray(byteArray, blockNumberOfPagesArray, index);
        index = ArrayUtils.insertByteArray(byteArray, blockPageIndexArray, index);

        return byteArray;
    }

    @Override
    public String toString() {
        return String.format(
            "Free: %b, Page type: %s, Block number of pages: %d, Block page index: %d",
            free,
            pageType,
            blockNumberOfPages,
            blockPageIndex
        );
    }

    public final static int PAGE_HEADER_SIZE = 16;
    public final static int PAGE_SIZE = 128;
    public final static int PAGE_TOTAL_SIZE = PAGE_HEADER_SIZE + PAGE_SIZE;
}
