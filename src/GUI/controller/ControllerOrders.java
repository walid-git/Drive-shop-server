package GUI.controller;

import GUI.MyListCell;
import Util.Order;
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

public class ControllerOrders {


    public volatile ObservableList<Order> orders = FXCollections.observableArrayList();
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
        ordersListView.setItems(orders);
        ordersListView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
            @Override
            public ListCell<Order> call(ListView<Order> param) {

                return new MyListCell();
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

}
