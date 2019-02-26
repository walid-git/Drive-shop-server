package Network;

import Util.Order;
import Util.Queue;
import backend.Robot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class RobotsCoroller {

    private static LinkedList<Robot> busyRobots = new LinkedList<Robot>();
    private boolean run = true;
    private static Queue<Order> ordersQueue;
    private static  Queue<Robot> robotsQueue = new Queue<Robot>();
    static Thread currentThread;
    public static void stop() {
        currentThread.interrupt();
    }


    public static void start() {
       currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Order order;
                        order = getOrdersQueue().pop();
                        System.out.println("Robot controllerOrders : new Order received");
                        Robot r = getRobotsQueue().pop();
                        System.out.println("Robot controllerOrders : Robot available");
                        r.processOrder(order);
                    } catch (InterruptedException e) {
                        System.out.println("Robot controller interrupted ");
                        break;
                    }
                }
            }
        },"RobotsController");
       currentThread.start();
    }


    public static Queue<Order> getOrdersQueue() {
        return ordersQueue;
    }

    public static void setOrdersQueue(Queue<Order> ordersQueue) {
        RobotsCoroller.ordersQueue = ordersQueue;
    }

    public static Queue<Robot> getRobotsQueue() {
        return robotsQueue;
    }

    public static void setRobotsQueue(Queue<Robot> robotsQueue) {
        RobotsCoroller.robotsQueue = robotsQueue;
    }
}
