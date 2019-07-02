package Background;

import Shared.Order;
import database.DBHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyOrdersServer extends Server{

    public MyOrdersServer(DBHandler handler) {
        super("MyOrdersServer",handler);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5566);
            while (running) {
                System.out.println("MyOrders Server : waiting for client...");
                Socket socket = serverSocket.accept();
                System.out.println("MyOrders Server : Client connected");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                            while ((inputStream.available()<1)) ;
                            long userId = inputStream.readLong();
                            ArrayList<Order> orders = handler.querryOrders(userId);
                            for (Order o : orders) {
                                outputStream.writeObject(o);
                                System.out.println("object written ");
                            }
                            outputStream.flush();
                            System.out.println("MyOrders Server : orders sent for user "+userId);
                            inputStream.close();
                            outputStream.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println("MyOrders server shut down "+e.toString());
        }
    }


}
