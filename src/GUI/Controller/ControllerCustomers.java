package GUI.Controller;


import GUI.CustomerListCell;
import GUI.Main;
import GUI.PromptDialog;
import Shared.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerCustomers {

    ObservableList<Customer> customers;

    @FXML
    public ListView<Customer> customersListView;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField searchBar;

    @FXML
    private Label phone;

    @FXML
    private Label name;

    @FXML
    private Label id;

    @FXML
    private Label birthDate;

    @FXML
    private Label email;


    @FXML
    void initialize() {
        customers = FXCollections.observableList(Main.handler.querryCustomers());
        customersListView.setPlaceholder(new Label("No customers, press add to create a new one"));
        customersListView.setCellFactory(param -> new CustomerListCell());
        customersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                name.setText(newValue.getLastName() + " " + newValue.getFirstName());
                phone.setText(newValue.getPhone());
                email.setText(newValue.getEmail());
                birthDate.setText(newValue.getBirthDate());
                id.setText(newValue.getId() + "");
                editButton.setDisable(false);
                removeButton.setDisable(false);
            } else {
                name.setText("Customer");
                phone.setText("");
                email.setText("");
                birthDate.setText("");
                id.setText("");
                editButton.setDisable(true);
                removeButton.setDisable(true);
            }
        });
        FilteredList<Customer> filteredList = new FilteredList<>(customers, null);
        searchBar.textProperty().addListener(obs -> {
            filteredList.setPredicate(c -> {
                String input = searchBar.getText();
                try {
                    return c.getId() == Integer.parseInt(input) || (c.getFirstName() + " " + c.getLastName()).toLowerCase().contains(searchBar.getText().toLowerCase());
                } catch (NumberFormatException e) {
                    return (c.getFirstName() + " " + c.getLastName()).toLowerCase().contains(searchBar.getText().toLowerCase());
                }

            });
        });
        customersListView.setItems(filteredList);
       /*
        FilteredList<Product> filteredList = new FilteredList<>(products,null);
        searchBar.textProperty().addListener(obs ->{
            filteredList.setPredicate(p -> {
                String input = searchBar.getText();
                try {
                    return p.getId() == Integer.parseInt(input) || p.getName().toLowerCase().startsWith(searchBar.getText().toLowerCase());
                } catch (NumberFormatException e) {
                    return p.getName().toLowerCase().startsWith(searchBar.getText().toLowerCase());
                }

            });
        });
        productsListView.setItems(filteredList);*/
    }

    @FXML
    void edit(ActionEvent event) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.mainStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/layout/newCustomer.fxml"));
        try {
            Parent root = loader.load();
            Scene sc = new Scene(root);
            stage.setScene(sc);
            stage.show();
            ((ControllerNewCustomer) loader.getController()).setParent(this);
            ((ControllerNewCustomer) loader.getController()).setEdit(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void remove(ActionEvent event) {
        boolean b = PromptDialog.prompt("are you sure you want to delete this customer ?");
        if (Main.handler.deleteCustomer(customersListView.getSelectionModel().getSelectedItem()) > 0)
            customers.remove(customersListView.getSelectionModel().getSelectedIndex());
    }

    @FXML
    void newCustomer(ActionEvent event) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.mainStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/layout/newCustomer.fxml"));
        try {
            Parent root = loader.load();
            Scene sc = new Scene(root);
            stage.setScene(sc);
            stage.show();
            ((ControllerNewCustomer) loader.getController()).setParent(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
