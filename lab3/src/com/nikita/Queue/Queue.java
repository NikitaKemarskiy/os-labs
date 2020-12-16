package com.nikita.Queue;

import java.util.ArrayList;

public class Queue<T> {
    private Item<T> head;
    private int priority;

    private Item<T> getLast() {
        Item<T> curr = head;

        while (head != null && head.getNext() != null) {
            curr = head.getNext();
        }

        return curr;
    }

    public Queue(int priority) {
        this.priority = priority;
    }

    public T pop() {
        if (isEmpty()) { return null; }

        T item = head.getItem();
        head = head.getNext();

        return item;
    }

    public void push(T item) {
        Item<T> last = getLast();
        if (last == null) {
            head = new Item(item);
        } else {
            last.setNext(new Item(item));
        }
    }

    public boolean isEmpty() {
        return head == null;
    }
}
