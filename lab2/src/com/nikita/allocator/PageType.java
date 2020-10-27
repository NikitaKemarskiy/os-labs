package com.nikita.allocator;

import java.util.HashMap;
import java.util.Map;

public enum PageType {
    EMPTY(0, "Empty"),
    BLOCK_SIZE_4(4, "4B blocks"),
    BLOCK_SIZE_16(16, "16B blocks"),
    BLOCK_SIZE_32(32, "32B blocks"),
    BLOCK_PAGE_SIZE(Integer.MAX_VALUE, "Page size block");

    private int size;
    private String alias;
    private static Map map = new HashMap<>();

    PageType(int size, String alias) {
        this.size = size;
        this.alias = alias;
    }

    static {
        for (PageType pageType : PageType.values()) {
            map.put(pageType.size, pageType);
        }
    }

    public static PageType valueOf(int size) {
        return (PageType) map.get(size);
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return alias;
    }
}
