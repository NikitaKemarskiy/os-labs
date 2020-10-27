package com.nikita;

import com.nikita.allocator.Allocator;
import com.nikita.allocator.InvalidIndexException;
import com.nikita.allocator.InvalidSizeException;
import com.nikita.util.ArrayUtils;

public class Main {
    public static void main(String[] args) {

        try {
            Allocator allocator = new Allocator(2000);
            System.out.println(allocator.buffer.length);

            int ind1 = allocator.alloc(10);
            int ind2 = allocator.alloc(16);
            int ind3 = allocator.alloc(12);
            int ind4 = allocator.alloc(300);

            System.out.println("Ind1: " + ind1);
            System.out.println("Ind2: " + ind2);
            System.out.println("Ind3: " + ind3);
            System.out.println("Ind4: " + ind4);

            // ind3 = allocator.realloc(ind3, 20);

            // System.out.println("Ind3 (realloc): " + ind3);

            allocator.write(ind1, new byte[]{1, 4, 5, 8, 1, 4, 5, 8, 1, 4, 5, 8});
            allocator.write(ind2, new byte[]{9, 6, 2, 125, 6});
            allocator.write(ind3, new byte[]{2, 2, 8, 1, 4, 8, 8});

            System.out.println("Block " + ind1 + ": " + ArrayUtils.byteArrayToString(allocator.read(ind1)));
            System.out.println("Block " + ind2 + ": " + ArrayUtils.byteArrayToString(allocator.read(ind2)));
            System.out.println("Block " + ind3 + ": " + ArrayUtils.byteArrayToString(allocator.read(ind3)));

            System.out.println(allocator.dump());
        } catch (InvalidIndexException err) {
            System.err.println(err);
            System.exit(1);
        }
    }
}
