package GUI.Controller;

import GUI.Dialog.ErrorDialog;
import Utils.ImgUtils;
import GUI.LocationPickerStage;
import GUI.Main;
import Shared.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;

public class ControllerAddProduct {

    private ControllerProducts parent;

    private boolean edit = false;

    @FXML
    private TextField pLocation;

    @FXML
    private TextField available_qty;

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
        try {
            if (checkInput()) {
                Product p = new Product(0,
                        Integer.parseInt(price.getText()),
                        name.getText(),
                        description.getText(),
                        Integer.parseInt(available_qty.getText().toString()),
                        path.getText(),
                        ImgUtils.imgToBytes(path.getText()),
                        Integer.parseInt(pLocation.getText()));
                if (!edit) {
                    int index = Main.handler.insertProduct(p);
                    if (index > 0) {
                        p.setId(index);
                        parent.products.add(p);
                    }

                } else {
                    Product selected = parent.productsListView.getSelectionModel().getSelectedItem();
                    p.setId(selected.getId());
                    int index = Main.handler.upadteProduct(selected, p, path.getText());
                    if (index > 0) {
                        System.out.println(index + " update");
                        parent.products.set(parent.productsListView.getSelectionModel().getSelectedIndex(), p);
                    }
                }
                cancel(event);
            }
        } catch (SQLException e) {
            ErrorDialog.show(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            ErrorDialog.show("Input error");
            e.printStackTrace();
        }

    }

    private boolean checkInput() {
        //TODO : check data validity here

        return true;
    }

    @FXML
    void setLocation(ActionEvent event) {
        long location = LocationPickerStage.pickLocation((Stage) ((Button) event.getSource()).getParent().getScene().getWindow());
        if (location != -1)
            pLocation.setText(location + "");
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
            price.setText(selected.getPrice() + "");
            path.setText(selected.getIconPath());
            available_qty.setText(selected.getAvailable_qty()+"");
            pLocation.setText(selected.getLocation() + "");
        }

    }

    public void setParent(ControllerProducts parent) {
        this.parent = parent;
    }
}
