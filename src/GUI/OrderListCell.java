package GUI;

import Shared.Order;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class OrderListCell extends ListCell<Order> {


    @Override
    protected void updateItem(Order item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText("");
            setGraphic(null);
        } else {
            Label id = new Label(item.getId() + "");
            id.setFont(Font.font("", FontWeight.BOLD, 25));
            VBox vBox = new VBox(id, new Label("User: "+item.getCustomerId() + ""));
            setGraphic(vBox);
        }
    }
}
