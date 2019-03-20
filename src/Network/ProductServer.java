package Network;

import Shared.Product;
import database.DBHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ProductServer extends Thread {

    private ArrayList<Product> products;
    ServerSocket serverSocket;
    volatile boolean runninig = true;
    DBHandler handler;

    public ProductServer(ArrayList<Product> products) {
        super("ProductsServerThread");
        this.products = products;
    }

    public ProductServer(DBHandler handler) {
        this.handler = handler;
    }

    public ProductServer() {
        super("ProductsServerThread");
    }


    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void kill() {
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
            serverSocket = new ServerSocket(5563);
            while (runninig) {
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

