package com.nikita;

import com.nikita.allocator.Allocator;
import com.nikita.allocator.InvalidIndexException;
import com.nikita.allocator.InvalidSizeException;

public class Main {
    public static void main(String[] args) {

        try {
            Allocator allocator = new Allocator(100);
            int ind1 = allocator.alloc(40);
            int ind2 = allocator.alloc(32);

            System.out.println(ind1);
            System.out.println(ind2);

            // allocator.free(ind1);
            // allocator.free(ind2);

            System.out.println(allocator.dump());
        } catch (InvalidSizeException err) {
            System.err.println(err);
            System.exit(1);
        }
    }
}
