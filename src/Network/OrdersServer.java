package Network;

import Util.Order;
import Util.Queue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class OrdersServer {

    public static volatile LinkedList<Order> pendingOrders = new LinkedList<Order>();
    private static Queue<Order> ordersQueue;
    static ServerSocket serverSocket;
    static volatile boolean runninig=true;

    public static void stop() {
        runninig = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startOrdersServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(5564);
                    while (runninig) {
                        System.out.println("Orders server : waiting for client...");
                        Socket socket = serverSocket.accept();
                        System.out.println("Orders server : Client connected");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                                    Order order;
                                    while ((order = (Order) (inputStream.readObject())) != null) {
                                        ordersQueue.add(order);
                                       /* if (main.controllerOrders != null) {
                                            Order finalOrder = order;
                                            Platform.runLater(()->main.controllerOrders.orders.add(finalOrder));
                                            System.out.println("OdersServer : order added to observable list");
                                        }*/
                                        System.out.println("OdersServer : order added to queue");
                                    }
                                    inputStream.close();
                                    outputStream.close();
                                } catch (IOException e) {
                                    //e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    //e.printStackTrace();
                                } finally {
                                    try {
                                        socket.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }).start();
                    }

                } catch (IOException e) {
                    System.out.println("Orders server shut down");
                }
            }
        },"OrdersServer").start();
    }

    public static Queue<Order> getOrdersQueue() {
        return ordersQueue;
    }

    public static void setOrdersQueue(Queue<Order> ordersQueue) {
        OrdersServer.ordersQueue = ordersQueue;
    }
}
