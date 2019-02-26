package Network;

import Util.Product;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ProductServer extends Thread {

    private ArrayList<Product> products;
    ServerSocket serverSocket;
    volatile boolean runninig = true;

    public ProductServer(ArrayList<Product> products) {
        super("ProductsServerThread");
        this.products = products;
    }

    public ProductServer() {
        super("ProductsServerThread");
    }

    public static byte[] imgToBytes(String file) {
        File imgFile = new File(file);
        try {
            BufferedImage img = ImageIO.read(imgFile);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
            System.out.println("Products server shut down");
        }
    }
}

