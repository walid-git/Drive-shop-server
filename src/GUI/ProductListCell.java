package GUI;

import GUI.Controller.ControllerOrders;
import GUI.Controller.ControllerProductListCell;
import Util.Order;
import Util.Product;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
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
            System.out.println("created item " + (++id));
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
            controller.icon.setImage(ImgUtils.getImageFromByteArray(item.getTab()));
            controller.name.setText(item.getName());
            controller.id.setText("id : " + item.getId());
            setGraphic(root);
        }
    }


}
