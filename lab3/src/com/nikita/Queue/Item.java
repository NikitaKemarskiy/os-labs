package com.nikita.Queue;

public class Item<T> {
    private T item;
    private Item<T> next;

    public Item(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    public Item<T> getNext() {
        return next;
    }

    public void setNext(Item<T> next) {
        this.next = next;
    }
}
