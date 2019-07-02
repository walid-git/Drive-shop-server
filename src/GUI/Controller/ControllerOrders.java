package GUI.Controller;

import GUI.*;
import Shared.*;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import Observer.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ControllerOrders implements Observer{


    public volatile ObservableList<Order> orders;

    @FXML
    private StackPane stackPane;

    Map map;

    @FXML
    private Button details;

    @FXML
    private ListView<Order> ordersListView;



    @FXML
    void initialize() {
        map = new Map(stackPane, 20,"default.map");
        Robot robot =new VirtualRobot(map.getDimension() - 1, map.getDimension() - 1);
//        Robot robot = new Robot(map.getDimension() - 1, map.getDimension() - 1,"192.168.43.89",1234);//"192.168.1.37" "192.168.43.89"
        map.addRobot(robot);
        Main.robotsQueue.add(robot);
        robot.setPosition(19);
        robot.setOrientation(4);
        /*Robot robot2 =new VirtualRobot(map.getDimension() - 1, map.getDimension() - 1);
        map.addRobot(robot2);
        Main.robotsQueue.add(robot2);
        robot2.setPosition(35);
        robot2.setOrientation(1);*/
        orders = FXCollections.observableArrayList();//Main.ordersQueue
        Main.ordersQueue.addObserver(this);
        ordersListView.setPlaceholder(new Label("Waiting for orders ..."));
        ordersListView.setItems(orders);
        ordersListView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
            @Override
            public ListCell<Order> call(ListView<Order> param) {
                return new OrderListCell();
            }
        });
        ordersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->details.setDisable(newValue==null));
    }

    @FXML
    void showOrderDetails(ActionEvent event) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.mainStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/layout/orderDetails.fxml"));
        try {
            Parent root = loader.load();
            Scene sc = new Scene(root);
            stage.setScene(sc);
            stage.show();
            ControllerOrderDetails controller = (ControllerOrderDetails) loader.getController();
            controller.setSubOrders(getSubOrders());
            controller.setOrder(ordersListView.getSelectionModel().getSelectedItem());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<SubOrder> getSubOrders() {
        ArrayList<SubOrder> subOrders = new ArrayList<>(orders.size());
        for (SendableSubOrder o : ordersListView.getSelectionModel().getSelectedItem().getSendableSubOrders())
            subOrders.add(new SubOrder(Main.handler.querrySingleProduct(o.getProductID()), o.getQuantity()));
        return subOrders;
    }

    @Override
    public void update(Observable o) {
        System.out.println("OBSEREV ::: Calling on change");
      /*  if (o instanceof Queue) {
            Queue<Order> q = (Queue) o;
            if (q.size()>orders.size())
                Platform.runLater(() -> orders.add(q.getLast()));
        }*/
    }
}
