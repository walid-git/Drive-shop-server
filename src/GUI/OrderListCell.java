package GUI;

import Observer.Observable;
import Observer.Observer;
import Shared.Order;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class OrderListCell extends ListCell<Order> implements Observer {


    Label id;
    Label state;
    VBox vBox;
    Order oldItem;

    public OrderListCell() {
        id = new Label();
        id.setFont(Font.font("", FontWeight.BOLD, 25));
        state = new Label();
        vBox = new VBox(id, state);
    }



    @Override
    protected void updateItem(Order item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText("");
            setGraphic(null);
        } else {
            if (oldItem != null) {
                oldItem.removeObserver(this);
            }
            oldItem = item;
            item.addObserver(this);
            id.setText(item.getId() + "");
            state.setText(("State: " + item.getOrderState() + ""));
            setGraphic(vBox);
        }
    }

    @Override
    public void update(Observable o) {
        if (o instanceof Order) {
            Order order = (Order) o;
            Platform.runLater(() -> state.setText(("State: " + order.getOrderState() + "")));

        }
    }
}
