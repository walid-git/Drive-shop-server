package Network;

import Util.Order;
import Util.Queue;
import backend.Robot;

import java.util.LinkedList;

public class RobotsController extends Thread {

    private LinkedList<Robot> busyRobots = new LinkedList<Robot>();
    private volatile Queue<Order> ordersQueue;
    private volatile Queue<Robot> robotsQueue;
    private volatile boolean running = true;

    public RobotsController(Queue<Order> ordersQueue, Queue<Robot> robotsQueue) {
        super("RobotsControllerThread");
        this.ordersQueue = ordersQueue;
        this.robotsQueue = robotsQueue;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Order order;
                order = this.ordersQueue.pop();
                System.out.println("Robot controllerOrders : new Order received");
                Robot r = this.robotsQueue.pop();
                System.out.println("Robot controllerOrders : Robot available");
                r.processOrder(order);
            } catch (Exception e) {
                System.out.println("Robot controller interrupted ");
                break;
            }
        }
    }

    public void kill() {
        running = false;
        this.interrupt();
    }


}
