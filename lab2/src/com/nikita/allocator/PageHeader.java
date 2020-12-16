package com.nikita.allocator;

import com.nikita.util.ArrayUtils;
import com.nikita.util.ByteConverter;

public class PageHeader {
    private PageType pageType;

    public PageHeader() {
        pageType = PageType.EMPTY;
    }

    public PageHeader(byte[] byteArray) {
        byte[] blockTypeByteArray = ArrayUtils.splitByteArray(byteArray, 0, 4);
        this.pageType = PageType.valueOf(ByteConverter.byteArrayToInt(blockTypeByteArray));
    }

    public byte[] toByteArray() {
        byte[] byteArray = new byte[PAGE_HEADER_SIZE];
        byte[] blockTypeArray = ByteConverter.intToByteArray(pageType.getSize());
        int index = 0;
        index = ArrayUtils.insertByteArray(byteArray, blockTypeArray, index);
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
            "Page type: %s",
            pageType
        );
    }

    public static PageType getPageTypeBySize(int size) {
        int totalSize = size;
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

    public final static int PAGE_HEADER_SIZE = 4;
    public final static int PAGE_SIZE = 360;
    public final static int PAGE_TOTAL_SIZE = PAGE_HEADER_SIZE + PAGE_SIZE;
}
