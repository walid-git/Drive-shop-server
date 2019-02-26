package GUI.controller;

import Util.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ControllerProducts {


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
/*
        ordersListView.setItems(orders);
        ordersListView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
            @Override
            public ListCell<Order> call(ListView<Order> param) {

                return new MyListCell();
            }
        });
*/

    }

    @FXML
    void showOrderDetails(ActionEvent event) {

    }

}
