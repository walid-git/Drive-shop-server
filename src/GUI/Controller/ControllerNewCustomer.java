package GUI.Controller;

import GUI.Dialog.ErrorDialog;
import GUI.Main;
import Shared.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControllerNewCustomer {

    private ControllerCustomers parent;

    private boolean edit = false;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phone;

    @FXML
    private Button addButton;

    @FXML
    private DatePicker birthDate;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passwordConfirm;

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void add(ActionEvent event) {
        try {
            if (checkInput()) {
                Customer c = new Customer(firstName.getText(),
                        lastName.getText().toUpperCase(),
                        birthDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")),
                        email.getText(),
                        phone.getText(),
                        password.getText());
                if (!edit) {
                    int index = Main.handler.insertCustomer(c);
                    if (index > 0) {
                        c.setId(index);
                        parent.customers.add(c);
                    }
                } else {
                    Customer selected = parent.customersListView.getSelectionModel().getSelectedItem();
                    c.setId(selected.getId());
                    int index = Main.handler.updateCustomer(selected, c);
                    if (index > 0) {
                        System.out.println(index + " update");
                        parent.customers.set(parent.customersListView.getSelectionModel().getSelectedIndex(), c);
                    }
                }
                cancel(event);
            }
        } catch (SQLException e) {
            ErrorDialog.show(e.getMessage());
        }
    }

    private boolean checkInput() {
        //TODO : check data validity here
        if (lastName.getText().contentEquals("") || firstName.getText().contentEquals("")) {
            ErrorDialog.show("Missing name !");
            return false;
        }
        return true;
    }

    @FXML
    void initialize() {
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
        if (edit) {
            addButton.setText("Edit");
            Customer selected = parent.customersListView.getSelectionModel().getSelectedItem();
            firstName.setText(selected.getFirstName());
            lastName.setText(selected.getLastName());
            email.setText(selected.getEmail());
            phone.setText(selected.getPhone());
//          birthDate.setPromptText(selected.getBirthDate());
            birthDate.setValue(LocalDate.parse(selected.getBirthDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        }

    }

    public void setParent(ControllerCustomers parent) {
        this.parent = parent;
    }
}
