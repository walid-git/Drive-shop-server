package Background;

import GUI.Main;
import GUI.Robot;
import Shared.Order;
import Shared.SendableSubOrder;
import Shared.SubOrder;
import Utils.Queue;

import java.util.ArrayList;

public class RobotsController extends BackgroundTask {

    private volatile Queue<Order> ordersQueue;
    private volatile Queue<Robot> robotsQueue;

    public RobotsController(Queue<Order> ordersQueue, Queue<Robot> robotsQueue) {
        super("RobotsControllerThread");
        this.ordersQueue = ordersQueue;
        this.robotsQueue = robotsQueue;
    }

    @Override
    public void run() {
        while (RobotsController.this.running) {
            try {
                Order order;
                order = this.ordersQueue.pop();
                System.out.println("Robot controllerOrders : new Order received");
                Robot r = this.robotsQueue.pop();
                System.out.println("Robot controllerOrders : Robot available");
                order.setOrderState(Order.State.IN_PROGRESS);
                Main.handler.updateOrderState(order);
                r.processOrder(order,getSubOrders(order.getSendableSubOrders()));
            } catch (Exception e) {
                System.out.println("Robot controller interrupted ");
                break;
            }
        }
    }
    @Override
    public void kill() {
        running = false;
        this.interrupt();
    }

    private ArrayList<SubOrder> getSubOrders(ArrayList<SendableSubOrder> orders) {
        ArrayList<SubOrder> subOrders = new ArrayList<>(orders.size());
        for (SendableSubOrder o : orders)
            subOrders.add(new SubOrder(Main.handler.querrySingleProduct(o.getProductID()), o.getQuantity()));
        return subOrders;
    }

}
