package GUI.Controller;

import Utils.ImgUtils;
import GUI.Main;
import GUI.ProductListCell;
import GUI.Dialog.PromptDialog;
import Shared.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerProducts {

    public volatile ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML
    public ListView<Product> productsListView;

    FilteredList<Product> filteredList;

    @FXML
    private TextField searchBar;

    @FXML
    private ImageView image;

    @FXML
    private Button buttonEdit;

    @FXML
    private Button buttonDelete;

    @FXML
    private Label description;

    @FXML
    private Label name;

    @FXML
    private Label available_qty;

    @FXML
    private Label price;

    @FXML
    private Label id;

    @FXML
    void initialize() {
        products = FXCollections.observableList(Main.handler.querryProducts());
        productsListView.setPlaceholder(new Label("No products to show, press Add new to create a new one"));
        productsListView.setCellFactory(param -> {
            return new ProductListCell();
        });
        productsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        name.setText(newValue.getName());
                        id.setText("ID: " + newValue.getId());
                        price.setText(newValue.getPrice() + "DA");
                        description.setText(newValue.getDescription());
                        available_qty.setText("Available : "+newValue.getAvailable_qty());
                        buttonDelete.setDisable(false);
                        buttonEdit.setDisable(false);
                        if (newValue.getImg().length > 0)
                            image.setImage(ImgUtils.getImageFromByteArray(newValue.getImg()));
                        else
                            image.setImage(null);
                    } else {
                        name.setText("");
                        id.setText("");
                        price.setText("");
                        description.setText("");
                        available_qty.setText("");
                        buttonDelete.setDisable(true);
                        buttonEdit.setDisable(true);
                        image.setImage(null);
                    }
                }
        );
        filteredList = new FilteredList<>(products, null);
        searchBar.textProperty().addListener(obs -> {
            filteredList.setPredicate(p -> {
                String input = searchBar.getText();
                try {
                    return p.getId() == Integer.parseInt(input) || p.getName().toLowerCase().startsWith(searchBar.getText().toLowerCase());
                } catch (NumberFormatException e) {
                    return p.getName().toLowerCase().startsWith(searchBar.getText().toLowerCase());
                }

            });
        });
        productsListView.setItems(filteredList);
    }

    @FXML
    void newProduct(ActionEvent event) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.mainStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/layout/addProduct.fxml"));
        try {
            Parent root = loader.load();
            Scene sc = new Scene(root);
            stage.setScene(sc);
            stage.show();
            ((ControllerAddProduct) loader.getController()).setParent(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void delete(ActionEvent event) {
        boolean b = PromptDialog.prompt("are you sure you want to delete this product ?");
        if (Main.handler.deleteProduct(productsListView.getSelectionModel().getSelectedItem()) > 0)
            products.remove(productsListView.getSelectionModel().getSelectedIndex());

    }

    @FXML
    void edit(ActionEvent event) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.mainStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/layout/addProduct.fxml"));
        try {
            Parent root = loader.load();
            Scene sc = new Scene(root);
            stage.setScene(sc);
            stage.show();
            ((ControllerAddProduct) loader.getController()).setParent(this);
            ((ControllerAddProduct) loader.getController()).setEdit(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload(){
        System.out.println("ControllerProducts.reload");
        int idx = productsListView.getSelectionModel().getSelectedIndex();
        products = FXCollections.observableList(Main.handler.querryProducts());
        filteredList = new FilteredList<>(products, null);
        productsListView.setItems(filteredList);
        productsListView.getSelectionModel().select(idx);
    }
}
