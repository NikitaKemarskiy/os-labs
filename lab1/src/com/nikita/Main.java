package com.nikita;

import com.nikita.allocator.Allocator;
import com.nikita.allocator.InvalidIndexException;
import com.nikita.allocator.InvalidSizeException;
import com.nikita.util.ArrayUtils;

public class Main {
    public static void main(String[] args) {

        try {
            Allocator allocator = new Allocator(200);
            int ind1 = allocator.alloc(32);
            int ind2 = allocator.alloc(32);

            allocator.write(ind1, new byte[]{1, 4, 5, 8});
            allocator.write(ind2, new byte[]{9, 6, 2, 125, 6});

            allocator.realloc(ind2, 24);

            // System.out.println("Block " + ind1 + ": " + ArrayUtils.byteArrayToString(allocator.read(ind1)));
            // System.out.println("Block " + ind2 + ": " + ArrayUtils.byteArrayToString(allocator.read(ind2)));

            System.out.println(allocator.dump());
        } catch (InvalidSizeException | InvalidIndexException err) {
            System.err.println(err);
            System.exit(1);
        }
    }
}
