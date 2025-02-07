package GUI.Dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ErrorDialog {


    public static void show(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.setResizable(true);
        dialog.headerTextProperty().set(null);
        dialog.showAndWait();
    }

}
