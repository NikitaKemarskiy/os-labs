package com.nikita.util;

public class ArrayUtils {
    private ArrayUtils() {}

    public static int insertByteArray(byte[] arr1, byte[] arr2, int index) {
        for (int i = 0; i < arr2.length; i++) {
            if (index >= arr1.length) {
                break;
            }
            arr1[index++] = arr2[i];
        }
        return index;
    }

    public static byte[] splitByteArray(byte[] arr, int startIndex, int endIndex) {
        byte[] byteArray = new byte[endIndex - startIndex];
        int index = 0;
        for (int i = startIndex; i < endIndex; i++) {
            byteArray[index++] = arr[i];
        }
        return byteArray;
    }

    public static String byteArrayToString(byte[] arr) {
        String str = "[";
        for (int i = 0; i < arr.length; i++) {
            str += arr[i];
            str += i == arr.length - 1
                ? "]"
                : ", ";
        }
        return str;
    }
}
