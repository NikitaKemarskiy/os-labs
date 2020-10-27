package com.nikita.allocator;

import com.nikita.util.ArrayUtils;
import com.nikita.util.ByteConverter;

public class PageHeader {
    private PageType pageType;
    private int blockNumberOfPages;
    private int blockPageIndex;

    public PageHeader() {
        pageType = PageType.EMPTY;
    }

    public PageHeader(byte[] byteArray) {
        byte[] blockTypeByteArray = ArrayUtils.splitByteArray(byteArray, 0, 4);
        byte[] blockNumberOfPagesByteArray = ArrayUtils.splitByteArray(byteArray, 4, 8);
        byte[] blockPageIndexByteArray = ArrayUtils.splitByteArray(byteArray, 8, 12);

        this.pageType = PageType.valueOf(ByteConverter.byteArrayToInt(blockTypeByteArray));
        this.blockNumberOfPages = ByteConverter.byteArrayToInt(blockNumberOfPagesByteArray);
        this.blockPageIndex = ByteConverter.byteArrayToInt(blockPageIndexByteArray);
    }

    public byte[] toByteArray() {
        byte[] byteArray = new byte[PAGE_HEADER_SIZE];

        byte[] blockTypeArray = ByteConverter.intToByteArray(pageType.getSize());
        byte[] blockNumberOfPagesArray = ByteConverter.intToByteArray(blockNumberOfPages);
        byte[] blockPageIndexArray = ByteConverter.intToByteArray(blockPageIndex);

        int index = 0;
        index = ArrayUtils.insertByteArray(byteArray, blockTypeArray, index);
        index = ArrayUtils.insertByteArray(byteArray, blockNumberOfPagesArray, index);
        index = ArrayUtils.insertByteArray(byteArray, blockPageIndexArray, index);

        return byteArray;
    }

    public PageType getPageType() {
        return pageType;
    }

    public void setPageType(PageType pageType) {
        this.pageType = pageType;
    }

    @Override
    public String toString() {
        return String.format(
            "Page type: %s, Block number of pages: %d, Block page index: %d",
            pageType,
            blockNumberOfPages,
            blockPageIndex
        );
    }

    public static PageType getPageTypeBySize(int size) {
        int totalSize = size + BlockHeader.BLOCK_HEADER_SIZE;
        if (totalSize > 32) {
            return PageType.BLOCK_PAGE_SIZE;
        } else if (totalSize > 16) {
            return PageType.BLOCK_SIZE_32;
        } else if (totalSize > 4) {
            return PageType.BLOCK_SIZE_16;
        } else {
            return PageType.BLOCK_SIZE_4;
        }
    }

    public final static int PAGE_HEADER_SIZE = 12;
    public final static int PAGE_SIZE = 360;
    public final static int PAGE_TOTAL_SIZE = PAGE_HEADER_SIZE + PAGE_SIZE;
}
