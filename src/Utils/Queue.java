package Utils;

import Observer.Observable;
import Observer.Observer;

import java.util.ArrayList;
import java.util.LinkedList;

public class Queue<T> extends LinkedList<T> implements Observable{

    ArrayList<Observer> observers = new ArrayList<Observer>();
    public Queue() {
        super();
    }

    public synchronized boolean add(T item) {
        boolean b = super.add(item);
        System.out.println("ADD ::: NOTIFYING " + item.getClass().getName() + " Thread " + Thread.currentThread().getName());
        notifyObservers();
        notify();
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
        notifyObservers();
        return obj;
    }


    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers)
            o.update(this);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
