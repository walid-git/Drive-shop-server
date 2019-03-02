package Util;

import observer.Observable;
import observer.Observer;

import java.util.ArrayList;
import java.util.LinkedList;

public class Queue<T> extends LinkedList<T> implements Observable<Queue<T>>{

    ArrayList<Observer> observers = new ArrayList<Observer>();
    public Queue() {
        super();
    }

    public synchronized boolean add(T item) {
        boolean b = super.add(item);
        System.out.println("ADD ::: NOTIFYING " + item.getClass().getName() + " Thread " + Thread.currentThread().getName());
        notify();
        notifyObserver(this);
        return b;
    }


    public synchronized T pop() {
        while (this.size() < 1) {
            System.out.println("POP ::: GOING TO SLEEP " + Thread.currentThread().getName());
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("POP ::: WAKING UP " + Thread.currentThread().getName());
        T obj = super.poll();
        notifyObserver(this);
        return obj;
    }


    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObserver(Queue<T> object) {
        for (Observer o : observers) {
            o.onChange(object);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
