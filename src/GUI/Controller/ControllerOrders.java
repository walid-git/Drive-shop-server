package GUI.Controller;

import GUI.OrderListCell;
import Shared.*;
import backend.Queue;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import GUI.Main;
import observer.Observer;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerOrders implements Observer<Queue<Order>>{


    public volatile ObservableList<Order> orders;

    @FXML
    private Label orderIdLabel;

    @FXML
    private ListView<Order> ordersListView;

    @FXML
    private Label statusLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    void initialize() {
        orders = FXCollections.observableList(Main.ordersQueue);
        Main.ordersQueue.addObserver(this);
        ordersListView.setPlaceholder(new Label("Waiting for orders ..."));
        ordersListView.setItems(orders);
        ordersListView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
            @Override
            public ListCell<Order> call(ListView<Order> param) {
                return new OrderListCell();
            }
        });

        //TODO : to be removed
        Order o = new Order(2, 25);
        ArrayList<SendableSubOrder> sendableSubOrders = new ArrayList<>();
        sendableSubOrders.add(new SendableSubOrder(1, 21));
        sendableSubOrders.add(new SendableSubOrder(2, 3));
        sendableSubOrders.add(new SendableSubOrder(3, 5));
        sendableSubOrders.add(new SendableSubOrder(4, 7));
        o.setSendableSubOrders(sendableSubOrders);
        orders.add(o);
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
            ((ControllerOrderDetails) loader.getController()).setOrder(getSubOrders());
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
    public void onChange(Queue<Order> obj) {
        System.out.println("OBSEREV ::: Calling on change");
        Platform.runLater(()->ordersListView.setItems(FXCollections.observableList(obj)));

    }
}
