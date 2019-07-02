package GUI.Controller;

import javafx.fxml.FXML;

public class ControllerMain {
    @FXML
    private ControllerOrders ordersController;
    @FXML
    private ControllerProducts productsController;

    public ControllerOrders getOrdersController() {
        return ordersController;
    }

    public ControllerProducts getProductsController() {
        return productsController;
    }
}
