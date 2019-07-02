package Background;

import GUI.Main;
import Shared.Order;
import Utils.Queue;
import database.DBHandler;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class OrdersServer extends Server {

    private volatile Queue<Order> ordersQueue;

    public OrdersServer(Queue<Order> ordersQueue, DBHandler handler) {
        super("OrdersThread", handler);
        this.ordersQueue = ordersQueue;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5564);
            while (running) {
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
                                if (OrdersServer.this.handler.confirmOrder(order)) {
                                    outputStream.writeBoolean(true);
                                    outputStream.flush();
                                    order.setOrderState(Order.State.PENDING);
                                    long id = OrdersServer.this.handler.insertOrder(order);
                                    order.setId((int) id);
                                    OrdersServer.this.ordersQueue.add(order);
                                    Order finalOrder = order;
                                    Platform.runLater(() -> Main.controllerOrders.orders.add(finalOrder));
                                       /* if (main.controllerOrders != null) {
                                            Order finalOrder = order;
                                            Platform.runLater(()->main.controllerOrders.orders.add(finalOrder));
                                            System.out.println("OdersServer : order added to observable list");
                                        }*/
                                    System.out.println("OdersServer : order added to queue customer = " + order.getCustomerId());
                                }
                                else {
                                    outputStream.writeBoolean(false);
                                    System.out.println("OdersServer : order rejected" + order.getCustomerId());
                                    outputStream.flush();
                                }
                            }
                            inputStream.close();
                            outputStream.close();
                        } catch (IOException e) {
                            //e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            //e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
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
            System.out.println("Orders server shut down " + e.toString());
        }
    }

}
