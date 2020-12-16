package com.nikita.Util;

public class Util {
    private Util() {}

    public static int randomInt(int from, int to) {
        return from + (int) (Math.random() * (to - from));
    }
}
