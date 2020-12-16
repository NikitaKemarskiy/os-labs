package com.nikita;

import com.nikita.allocator.Allocator;
import com.nikita.allocator.InvalidIndexException;

public class Main {
    public static void main(String[] args) {
        try {
            Allocator allocator = new Allocator(1024);
            int ind1 = allocator.alloc(4);
//            allocator.write(ind1, new byte[]{1, 4, 8, 5});
//            allocator.realloc(ind1, 48);
            System.out.println(allocator.dump());
        } catch (RuntimeException err) {
            System.err.println(err);
            System.exit(1);
        }
    }
}
