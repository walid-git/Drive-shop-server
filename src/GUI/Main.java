package GUI;

import GUI.controller.ControllerOrders;
import Network.OrdersServer;
import Network.ProductServer;
import Util.Order;
import Util.Product;
import Util.Queue;
import backend.Robot;
import database.DBHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Network.RobotsCoroller;

import java.util.ArrayList;

import static Network.ProductServer.imgToBytes;

public class Main extends Application {

    public static Stage mainStage;
    public ControllerOrders controllerOrders;
    public static volatile Queue<Order> ordersQueue = new Queue<Order>();
    public static volatile Queue<Robot> robotsQueue = new Queue<Robot>();


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout/sample.fxml"));
        Parent root = loader.load();
        mainStage = primaryStage;
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        controllerOrders = loader.getController();
        System.out.println(controllerOrders == null ? "null" : "not null");
    }


    public static void main(String[] args) {
        OrdersServer.setOrdersQueue(ordersQueue);
        RobotsCoroller.setOrdersQueue(ordersQueue);
        RobotsCoroller.setRobotsQueue(robotsQueue);
        ProductServer.setProducts(randomProducts());
        ProductServer.startProductsServer();
        OrdersServer.startOrdersServer();
        RobotsCoroller.start();
        robotsQueue.add(new Robot(1, "192.168.43.130", 1234));
        launch(args);
//        DBHandler handler = new DBHandler();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("calling stop");
        ProductServer.stop();
        OrdersServer.stop();
        RobotsCoroller.stop();
    }

    public static ArrayList<Product> randomProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        for (int i = 0; i < 30; i++) {
            products.add(new Product(0, 25, "Hlib ", "Hlib ta3 chkara ta3 l onalait ta3 boudouaou", imgToBytes("/home/walid/Downloads/products/img1.png")));
            products.add(new Product(1, 80, "Candia ", "Hlib ta3 candia", imgToBytes("/home/walid/Downloads/products/img2.png")));
            products.add(new Product(2, 10, "Khobz ", "Le pain", imgToBytes("/home/walid/Downloads/products/img3.png")));
            products.add(new Product(3, 80, "Jus ", "Jus d'orange ta3 tchina ", imgToBytes("/home/walid/Downloads/products/img4.png")));
            products.add(new Product(4, 30, "Farina ", "La farine ", imgToBytes("/home/walid/Downloads/products/img5.png")));
            products.add(new Product(5, 16, "yaourt ", "Yogurt", imgToBytes("/home/walid/Downloads/products/img6.png")));
        }
        return products;
    }

}
