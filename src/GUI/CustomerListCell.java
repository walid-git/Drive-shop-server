package GUI;

import Shared.Customer;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CustomerListCell extends ListCell<Customer> {

    Label name;
    Label id;
    VBox vBox;

    public CustomerListCell() {
        super();
        name = new Label();
        id = new Label();
        name.setFont(Font.font("", FontWeight.BOLD, 22));
        id.setFont(Font.font(16));
        vBox = new VBox(name, id);
    }

    @Override
    protected void updateItem(Customer item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText("");
            setGraphic(null);
        } else {
            name.setText(item.getLastName() + " " + item.getFirstName());
            id.setText(item.getId() + "");
            setGraphic(vBox);
        }
    }
}
