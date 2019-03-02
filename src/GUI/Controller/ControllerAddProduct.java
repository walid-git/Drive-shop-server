package GUI.Controller;

import GUI.Main;
import Network.ProductServer;
import Util.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ControllerAddProduct {


    private ControllerProducts parent;

    private boolean edit = false;

    @FXML
    private TextField path;

    @FXML
    private Button addButton;

    @FXML
    private TextField price;

    @FXML
    private TextField name;

    @FXML
    private TextField description;

    @FXML
    void browse(ActionEvent event) {
       /* JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Icone file (.png)", "png");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int r = chooser.showOpenDialog(null);
        if (r==JFileChooser.APPROVE_OPTION)
            path.setText(chooser.getSelectedFile().getAbsolutePath());*/
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().removeAll();
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Icone file (.png)", "*.png")));
        File f = fileChooser.showOpenDialog(((Button) event.getSource()).getScene().getWindow());
        if (f != null)
            path.setText(f.getAbsolutePath());
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void add(ActionEvent event) {
        if (checkInput()) {
            Product p = new Product(0,
                    Integer.parseInt(price.getText()),
                    name.getText(),
                    description.getText(),
                    ProductServer.imgToBytes(path.getText()));
            if (!edit) {
                int index = Main.handler.insertProduct(p, path.getText());
                if (index > 0) {
                    p.setId(index);
                    parent.products.add(p);
                }

            } else {
                Product selected = parent.productsListView.getSelectionModel().getSelectedItem();
                p.setId(selected.getId());
                int index = Main.handler.upadteProduct(selected, p, path.getText());
                if (index > 0) {
                    System.out.println(index+" update");
                    parent.products.set(parent.productsListView.getSelectionModel().getSelectedIndex(), p);
                }
            }
            cancel(event);
        }
    }

    private boolean checkInput() {
        return true;
    }

    @FXML
    void initialize() {
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
        if (edit) {
            addButton.setText("Edit");
            Product selected = parent.productsListView.getSelectionModel().getSelectedItem();
            name.setText(selected.getName());
            description.setText(selected.getDescription());
            price.setText(selected.getPrice()+"");
            //TODO : get real path
            path.setText("/home/walid/Downloads/products/img2.png");
        }

    }

    public void setParent(ControllerProducts parent) {
        this.parent = parent;
    }
}
