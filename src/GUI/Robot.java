package GUI;

import Shared.Order;
import Shared.SubOrder;
import javafx.application.Platform;
import Observer.Observable;
import Observer.Observer;
import GUI.Dialog.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Robot implements Observable {

    private volatile int x;
    private volatile int y;
    private Map.Cell[][] obstaclesMap;
    private Map map;
    private int mapDimens;
    short[][] path;
    private long travelSpeed = 350;
   


    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    ArrayList<Observer> observers = new ArrayList<Observer>();

    public Robot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        notifyObservers();
    }

    public void setPosition(int node) {
        this.x = node % mapDimens;
        this.y = node / mapDimens;
        notifyObservers();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        notifyObservers();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        notifyObservers();
    }

    public int getNode() {
        return y * mapDimens + x;
    }

    public void setMap(Map myMap) {
        this.map = myMap;
        this.obstaclesMap = myMap.getMap();
        this.mapDimens = myMap.getDimension();
    }

    public void moveTo(int dest) {
//        map.clearVisitedNodes();
        map.setDestination(dest);
        map.setStart(getNode());

        this.floyd(dest);
    }

    private void floyd(int dest) {
        path = map.getP();
        if (path[getNode()][dest] != -2) {
            LinkedList<Integer> path = new LinkedList<Integer>();
            getPath(path, getNode(), dest);
            traversePathR(path);
        }else
            Platform.runLater(() -> InfoDialog.show("There is no possible path"));
    }

    private void getPath(LinkedList<Integer> path, int src, int dest) {
        if (this.path[src][dest] == -1) {
            path.addLast(dest);
        } else {
            getPath(path, src, this.path[src][dest]);
            if (path.getLast() != dest)
                getPath(path, path.getLast(), dest);
        }

    }

    void traversePathR(LinkedList<Integer> path) {
        while (!path.isEmpty()) {
            setPosition(path.pollFirst());
            try {
                Thread.sleep(450 - travelSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setTravelSpeed(long travelSpeed) {
        this.travelSpeed = travelSpeed;
    }

    public void processOrder(Order o,ArrayList<SubOrder> subOrders) {
        new Thread(() -> {
            for (SubOrder subOrder : subOrders) {
                System.out.println("moving to product : "+subOrder.getProduct().getName());
                floyd(subOrder.getProduct().getLocation());
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            floyd(343);
            floyd(383);
            floyd(399);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            floyd(39);
            o.setOrderState(Order.State.READY);
            Main.robotsQueue.add(this);
        }).start();
    }
}
