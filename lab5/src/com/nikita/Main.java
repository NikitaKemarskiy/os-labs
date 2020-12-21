package com.nikita;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        int size = 200;

        long initialExecutionTime = initial(size);
        long improvedExecutionTime = improved(size);

        System.out.printf("- Initial code execution time: %d%n", initialExecutionTime);
        System.out.printf("- Improved code execution time: %d%n", improvedExecutionTime);
    }

    public static long initial(int size) {
        long startTimestamp = new Date().getTime();

        int[][][] arr = new int[size][size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    arr[k][j][i]++;
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
}
