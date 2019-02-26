package Util;

import java.util.LinkedList;

public class Queue<T> {
    LinkedList<T> items;

    public Queue() {
        this.items = new LinkedList<T>();
    }

    public synchronized void add(T item) {
        items.add(item);
        System.out.println("ADD ::: NOTIFYING " + item.getClass().getName() + " Thread " + Thread.currentThread().getName());
        notify();
    }

    public synchronized T pop() throws InterruptedException {
        while (items.size() < 1) {
            System.out.println("POP ::: GOING TO SLEEP " + Thread.currentThread().getName());
            wait();
        }
        System.out.println("POP ::: WAKING UP " + Thread.currentThread().getName());
        return items.poll();
    }

}
