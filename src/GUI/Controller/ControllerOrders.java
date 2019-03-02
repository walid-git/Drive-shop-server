package GUI.Controller;

import GUI.OrderListCell;
import Util.Order;
import Util.Queue;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import GUI.Main;
import observer.Observer;

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
        ordersListView.setItems(orders);
        ordersListView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
            @Override
            public ListCell<Order> call(ListView<Order> param) {
                return new OrderListCell();
            }
        });
    }

    @FXML
    void showOrderDetails(ActionEvent event) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.mainStage);
        Scene sc = new Scene(new AnchorPane(), 400, 250);
        stage.setScene(sc);
        stage.show();
    }

    @Override
    public void onChange(Queue<Order> obj) {
        System.out.println("OBSEREV ::: Calling on change");
        Platform.runLater(()->ordersListView.setItems(FXCollections.observableList(obj)));

    }
}
