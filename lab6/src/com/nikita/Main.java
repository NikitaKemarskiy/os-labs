package com.nikita;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        int size = 200;

        long initialExecutionTime = initial(size);

        System.out.printf("- Initial code execution time: %d%n", initialExecutionTime);
    }

    public static long initial(int size) {
        long startTimestamp = new Date().getTime();

        for (int i = 0; i < 10; i++) {
            for (int j = (int) 1e6; j > 0; j--) {
                if (func1() || func2(i)) {
                    System.out.println(">>> Inside if [" + i + "][" + j + "]");
                }
            }
        }

        return new Date().getTime() - startTimestamp;
    }

    public static long improved(int size) {
        long startTimestamp = new Date().getTime();

        int[][][] arr = new int[size][size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    arr[i][j][k]++;
                }
            }
        }

        return new Date().getTime() - startTimestamp;
    }

    public static boolean func1() {
        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(100);
            }
        } catch (InterruptedException err) {
            System.err.println(err);
            System.exit(1);
        }
        return true;
    }
    public static boolean func2(int num) {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1);
                if (i > num) {
                    break;
                }
            }
        } catch (InterruptedException err) {
            System.err.println(err);
            System.exit(1);
        }
        return true;
    }
}