package GUI;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;

public class VirtualRobot extends Robot {

    static boolean running = true;
    static int vPort = 25845;
    static final long TURN_DELAY = 200;
    private Socket socket;
    private byte[] tab;
    public VirtualRobot(int x, int y) {
        super(x, y, "127.0.0.1", vPort++);
        new Thread(() -> {

            byte[] data = new byte[1000];


            try {
                ServerSocket serverSocket = new ServerSocket(this.port);
                while (running) {
                    tab = new byte[]{1, 0, 0};
                    System.out.println("accepting .... "+serverSocket.isBound());;

                    socket = serverSocket.accept();
                    System.out.println("VirtualRobot new connection ");
                    socket.getInputStream().read(data, 0, 1000);
//                    sendPacket = new DatagramPacket(tab, 3, receivedPacket.getAddress(), receivedPacket.getPort());
                    for (int i = 2; i < data[0]; i += 2) {
                        int pos = ((int)data[i] << 8) | ((data[i+1]&255));
                        System.out.println("\n\n\nVirtualRobot moving to " + pos);
                        checkOrientation(pos);
                        tab[0] = 1;
                        tab[1] = data[i];
                        tab[2] = data[i + 1];
                        socket.getOutputStream().write(tab,0,3);
                        socket.getOutputStream().flush();
                        Thread.sleep(300);
                    }
                    if (data[1]==1)
                        Thread.sleep(4000);// Pick up object
                    tab[0] = 0;
                    tab[1] = 0;
                    socket.getOutputStream().write(tab,0,3);
                    socket.getOutputStream().flush();
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ).start();
    }

    void checkOrientation(int dest) {
        int curNode = getNode();
        int val = curNode - dest;
        System.out.println("CHeck cur "+curNode+" dest "+dest+" val "+val);;
        if (val == -1) {
            changeOrientation((short) 2);
        } else if (val == 1) {
            changeOrientation((short) 4);
        } else if (val == this.mapDimens) {
            changeOrientation((short) 1);
        } else if (val == -this.mapDimens) {
            changeOrientation((short) 3);
        }

    }

    void changeOrientation(short o) {
        if (o == this.orientation)
            return;
        else {
            switch (this.orientation - o) {
                case -1 :
                    //turn Right x1
                    System.out.println("-1 = " + -1);
                    turnRight(1);
                    break;
                case 1 :
                    //turn Left x1
                    System.out.println("1 = " + 1);
                    turnLeft(1);
                    break;
                case -2 :
                    //turn Right x2
                    System.out.println("-2 = " + -2);
                    turnRight(2);
                    break;
                case 2 :
                    //turn Left x2
                    System.out.println("2 = " + 2);
                    turnLeft(2);
                    break;
                case 3 :
                    //turn Right x1
                    System.out.println("3 = " + 3);
                    turnRight(1);
                    break;
                case -3 :
                    //turn Left x1
                    System.out.println("-3 = " + -3);
                    turnLeft(1);
                    break;
            }


        }
    }

    void turnRight(int n) {
        System.out.println("VirtualRobot.turnRight");
        for (int i = 0; i < n; i++) {
            System.out.println("VirtualRobot.turnRight2 "+(this.orientation == 4?1:this.orientation+1));

            tab[0] = 2;
            tab[1] = (byte) (this.orientation == 4?1:this.orientation+1);
            try {
                socket.getOutputStream().write(tab,0,3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(TURN_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void turnLeft(int n) {
        System.out.println("VirtualRobot.turnLeft1");

        for (int i = 0; i < n; i++) {
            System.out.println("VirtualRobot.turnLeft2 orient"+( this.orientation == 1?4:this.orientation-1));
            tab[0] = 2;
            tab[1] = (byte) (this.orientation == 1?4:this.orientation-1);
            try {
                socket.getOutputStream().write(tab,0,3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(TURN_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
