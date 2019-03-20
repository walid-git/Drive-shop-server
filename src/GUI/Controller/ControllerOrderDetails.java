package GUI.Controller;

import GUI.SubOrderListCell;
import Shared.SubOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.ArrayList;


public class ControllerOrderDetails {
    ObservableList<SubOrder> subOrders = FXCollections.observableArrayList();

    @FXML
    private ListView<SubOrder> subOrdersListView;

    @FXML
    private Label totalPrice;

    @FXML
    private Label orderId;

    @FXML
    private Label customerId;



    @FXML
    private Label customerName;

    @FXML
    void initialize() {
       subOrdersListView.setItems(subOrders);
       subOrdersListView.setCellFactory(new Callback<ListView<SubOrder>, ListCell<SubOrder>>() {
           @Override
           public ListCell<SubOrder> call(ListView<SubOrder> param) {
               return new SubOrderListCell();
           }
       });
    }

    public void setOrder(ArrayList<SubOrder> subOrders) {
        this.subOrders.setAll(subOrders);
    }


}
