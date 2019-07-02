package GUI;

import Observer.Observable;
import Observer.Observer;
import Shared.Order;
import Shared.SubOrder;

import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Robot implements Observable {

    protected Map map;
    protected volatile int x;
    protected volatile int y;
    protected int mapDimens;
    protected ArrayList<Observer> observers = new ArrayList<Observer>();
    protected String ipAddress;
    protected int port;
    protected Socket socket;
    protected volatile int orientation = 1;

    public Robot(int x, int y, String ip, int port) {
        this.x = x;
        this.y = y;
        this.ipAddress = ip;
        this.port = port;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        notifyObservers();
    }

    public void setPosition(int node) {
        this.x = node % mapDimens;
        this.y = node / mapDimens;
        notifyObservers();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        notifyObservers();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        notifyObservers();
    }

    public int getNode() {
        return y * mapDimens + x;
    }

    public void moveTo(int node, boolean pause) {
        this.map.clearDestination();
        this.map.setDestination(node);
        byte[] data = getPath(getNode(), node);
        data[1] = (byte) (pause ? 1 : 0);
        try {
            socket = new Socket(ipAddress, port);
            System.out.println(Arrays.toString(data));
            socket.getOutputStream().write(data);
            socket.getOutputStream().flush();
            System.out.println("Robot.moveTo :: packet sent");
            while (true) {
                data = new byte[3];
                socket.getInputStream().read(data, 0, 3);
                System.out.println("Robot.moveTo :: packet received " + data[0] + "," + data[1] + "," + data[2]);
                if (data[0] == 0 && data[1] == 0) {
                    break;
                } else if (data[0] == 1) {
                    int pos = ((int) data[1] << 8) | ((data[2] & 255));
                    System.out.println("Moving to " + pos);
                    setPosition(pos);
                } else if (data[0] == 2) {
                    System.out.println("Robot. changing orientation " + data[1]);
                    setOrientation(data[1]);
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMap(Map myMap) {
        this.map = myMap;
//        this.obstaclesMap = myMap.getMap();
        this.mapDimens = myMap.getDimension();
        //@TODO To be continued
      /*  byte[] data = new byte[mapDimens * mapDimens+1];
        data[0] = 0;
        for (int i = 0; i < mapDimens; i++) {
            for (int j = 0; j < mapDimens; j++) {
                data[i*mapDimens+j+1]= (byte)myMap.getP()[i][j];
            }
        }
        try {
            DatagramPacket packet = new DatagramPacket(data,
                    data.length,
                    InetAddress.getByName(this.ipAddress),
                    this.port);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void processOrder(Order o, ArrayList<SubOrder> subOrders) {
        new Thread(() -> {
            System.out.println("Robot.processOrder :: start processing order ..");

            //Ordering products by closest one
            int src = getNode();
            for (int i = 0; i < subOrders.size(); i++) {
                int minIndex = i, minPath = getPath(src, subOrders.get(i).getProduct().getLocation()).length;
                for (int j = subOrders.size() - 1; j > i; j--) {
                    int length = getPath(src, subOrders.get(j).getProduct().getLocation()).length;
                    if (length < minPath) {
                        minPath = length;
                        minIndex = j;
                    }
                }
                SubOrder tmp = subOrders.get(minIndex);
                subOrders.set(minIndex, subOrders.get(i));
                subOrders.set(i, tmp);
                tmp = null;
                src = subOrders.get(i).getProduct().getLocation();
            }

            for (int i = 0; i < subOrders.size(); i++) {
                moveTo(subOrders.get(i).getProduct().getLocation(), true);
            }
            moveTo(343, false);
            moveTo(398, true);
            o.setOrderState(Order.State.READY);
            try {
                Main.handler.updateOrderState(o);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            moveTo(19, false);
            setOrientation(4);
            Main.robotsQueue.add(this);
        }).start();
       /* new Thread(() -> {
            for (SubOrder subOrder : subOrders) {
                System.out.println("moving to product : "+subOrder.getProduct().getName());
                floyd(subOrder.getProduct().getLocation());
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            floyd(343);
            floyd(383);
            floyd(399);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            floyd(39);
            o.setOrderState(Order.State.READY);
            Main.robotsQueue.add(this);
        }).start();*/
    }

    private byte[] getPath(int des) {
        ArrayList<Byte> path = new ArrayList<Byte>();
        short[][] p = map.getP();
        int node = getNode();
        while (p[node][des] != des) {
            path.add((byte) (p[node][des] >> 8));
            path.add((byte) (p[node][des] & 255));
            node = p[node][des];
        }
        path.add((byte) (des >> 8));
        path.add((byte) (des & 255));
        byte[] path1 = new byte[path.size() + 2];
        path1[0] = (byte) path.size();
        for (int i = 0; i < path.size(); i++) {
            path1[i + 2] = path.get(i);
        }
        return path1;
    }

    private byte[] getPath(int src, int des) {
        ArrayList<Byte> path = new ArrayList<Byte>();
        short[][] p = map.getP();
        while (p[src][des] != des) {
            path.add((byte) (p[src][des] >> 8));
            path.add((byte) (p[src][des] & 255));
            src = p[src][des];
        }
        path.add((byte) (des >> 8));
        path.add((byte) (des & 255));
        byte[] path1 = new byte[path.size() + 2];
        path1[0] = (byte) (path.size()+2);
        for (int i = 0; i < path.size(); i++) {
            path1[i + 2] = path.get(i);
        }
        return path1;
    }


    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        notifyObservers();
    }
}
