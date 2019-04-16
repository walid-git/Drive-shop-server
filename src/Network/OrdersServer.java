package Network;

import GUI.Main;
import Shared.Order;
import backend.Queue;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OrdersServer extends Thread{

    private volatile Queue<Order> ordersQueue;
    private ServerSocket serverSocket;
    private volatile boolean runninig=true;

    //@TODO to be romoved
    static int ID = 0;

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
                                order.setOrderState(Order.State.PENDING);
                                OrdersServer.this.ordersQueue.add(order);
                                Order finalOrder = order;
                                finalOrder.setId(ID++);
                                Platform.runLater(() -> Main.controllerOrders.orders.add(finalOrder));
                                       /* if (main.controllerOrders != null) {
                                            Order finalOrder = order;
                                            Platform.runLater(()->main.controllerOrders.orders.add(finalOrder));
                                            System.out.println("OdersServer : order added to observable list");
                                        }*/
                                System.out.println("OdersServer : order added to queue customer = "+order.getCustomerId());
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
            System.out.println("Orders server shut down "+e.toString());
        }
    }

}
