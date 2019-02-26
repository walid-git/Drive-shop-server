package backend;

import Util.Order;

import java.io.IOException;
import java.net.*;

public class Robot {

    private int id;
    private String ipAddress;
    private int portListening;

    public Robot(int id, String ipAddress, int portListening) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.portListening = portListening;
    }

    public InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getByName(getIpAddress());
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortListening() {
        return portListening;
    }

    public void setPortListening(int portListening) {
        this.portListening = portListening;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void processOrder(Order order) {
        try {
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(new byte[]{10, 5, 30, 25}, 4,this.getInetAddress(), this.getPortListening());
            socket.send(packet);
            System.out.println("UDP packet sent to robot "+this.getId()+" @"+this.getInetAddress().toString());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
