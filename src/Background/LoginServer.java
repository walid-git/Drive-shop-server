package Background;

import database.DBHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LoginServer extends Server {

    public LoginServer(DBHandler handler) {
        super("LoginServerThread",handler);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5565);
            while (running) {
                System.out.println("Login Server : waiting for client...");
                Socket socket = serverSocket.accept();
                System.out.println("Login Server : Client connected");
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                            System.out.println("Login server: waiting for input ");
                            while ((inputStream.available()<1)) ;
                            System.out.println("Login server: received login data ");
                            String[] data = inputStream.readUTF().split("::");
                            System.out.println("phone : "+data[0]+" password : "+data[1]);
                            outputStream.writeLong(handler.querryCustomerId(data[0],data[1]));
                            outputStream.flush();
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
            System.out.println("Login server shut down "+e.toString());
        }
    }
}

