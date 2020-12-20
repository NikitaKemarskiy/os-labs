package com.nikita.Queue;

public class Queue<T> {
    private Item<T> head;

    private Item<T> getLast() {
        Item<T> curr = head;

        while (curr != null && curr.getNext() != null) {
            curr = curr.getNext();
        }

        return curr;
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
