package com.nikita;

import com.nikita.allocator.Allocator;
import com.nikita.allocator.InvalidIndexException;
import com.nikita.allocator.InvalidSizeException;
import com.nikita.util.ArrayUtils;

public class Main {
    public static void main(String[] args) {

        try {
            Allocator allocator = new Allocator(600);
            System.out.println(allocator.buffer.length);

            int ind1 = allocator.alloc(10);
            int ind2 = allocator.alloc(16);
            int ind3 = allocator.alloc(12);
            int ind4 = allocator.alloc(18);

            System.out.println(ind1);
            System.out.println(ind2);
            System.out.println(ind3);
            System.out.println(ind4);


//            int ind1 = allocator.alloc(24);
//            int ind2 = allocator.alloc(32);
//
//            allocator.write(ind1, new byte[]{1, 4, 5, 8});
//            allocator.write(ind2, new byte[]{9, 6, 2, 125, 6});
//
//            allocator.realloc(ind2, 24);

            // System.out.println("Block " + ind1 + ": " + ArrayUtils.byteArrayToString(allocator.read(ind1)));
            // System.out.println("Block " + ind2 + ": " + ArrayUtils.byteArrayToString(allocator.read(ind2)));

            System.out.println(allocator.dump());
        } catch (/*InvalidSizeException*/ RuntimeException err) {
            System.err.println(err);
            System.exit(1);
        }
    }
}
