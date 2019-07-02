package Background;

import Shared.Product;
import database.DBHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ProductServer extends Server {

    private ArrayList<Product> products;

    public ProductServer(DBHandler handler) {
        super("ProductServer",handler);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5563);
            while (running) {
                System.out.println("Products Server : waiting for client...");
                Socket socket = serverSocket.accept();
                if (handler != null)
                    products = handler.querryProducts();
                System.out.println("Products Server : Client connected");
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                            for (Product p : products)
                                outputStream.writeObject(p);
                            outputStream.flush();
                            System.out.println("Products Server : Product sent");
                            inputStream.close();
                            outputStream.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println("Orders server shut down "+e.toString());
        }
    }
}

