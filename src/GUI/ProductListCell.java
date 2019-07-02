package GUI;

import GUI.Controller.ControllerProductListCell;
import Shared.Product;
import Utils.ImgUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class ProductListCell extends ListCell<Product> {

    static int id = 0;
    ControllerProductListCell controller;
    Parent root;

    public ProductListCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/layout/productListCell.fxml"));
            this.root = loader.load();
            this.controller = (ControllerProductListCell) loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Product item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText("");
            setGraphic(null);
        } else {
            if (item.getImg().length > 0)
                controller.icon.setImage(ImgUtils.getImageFromByteArray(item.getImg()));
            else
                controller.icon.setImage(null);
            controller.name.setText(item.getName());
            controller.id.setText("id : " + item.getId());
            setGraphic(root);
        }
    }


}
