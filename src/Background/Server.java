package Background;

import database.DBHandler;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class Server extends BackgroundTask {
    ServerSocket serverSocket;
    DBHandler handler;

    public Server(DBHandler handler){
        this.handler = handler;
    }

    public Server(String name, DBHandler handler) {
        super(name);
        this.handler = handler;
    }

    @Override
    public void kill() {
        this.running = false;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
