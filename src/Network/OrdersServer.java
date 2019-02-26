package Network;

import Util.Order;
import Util.Queue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class OrdersServer extends Thread{

    private volatile Queue<Order> ordersQueue;
    private ServerSocket serverSocket;
    private volatile boolean runninig=true;

    public OrdersServer(Queue<Order> ordersQueue) {
        super("OrdersThread");
        this.ordersQueue = ordersQueue;
    }

    public  void kill() {
        this.runninig = false;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                                OrdersServer.this.ordersQueue.add(order);
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

}
