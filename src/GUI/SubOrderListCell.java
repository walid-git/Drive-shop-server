package GUI;

import GUI.Controller.ControllerSubOrderListCell;
import Shared.SubOrder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class SubOrderListCell extends ListCell<SubOrder> {

    static int id = 0;
    ControllerSubOrderListCell controller;
    Parent root;

    public SubOrderListCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/layout/subOrderListCell.fxml"));
            this.root = loader.load();
            this.controller = (ControllerSubOrderListCell) loader.getController();
            System.out.println("created item " + (++id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(SubOrder item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText("");
            setGraphic(null);
        } else {
            controller.icon.setImage(ImgUtils.getImageFromByteArray(item.getProduct().getImg()));
            controller.name.setText(item.getProduct().getName());
            controller.id.setText("id : " + item.getProduct().getId());
            controller.quantity.setText(""+item.getQuantity());
            setGraphic(root);
        }
    }


}
