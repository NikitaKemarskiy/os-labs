package com.nikita.util;

import com.nikita.allocator.InvalidSizeException;

public class ArrayUtils {
    private ArrayUtils() {}

    public static int insertByteArray(byte[] arr1, byte[] arr2, int index) {
        for (int i = 0; i < arr2.length; i++) {
            arr1[index++] = arr2[i];
        }
        return index;
    }
}
