package GUI;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ErrorDialog {


    public static void show(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.headerTextProperty().set(null);
        dialog.showAndWait();
    }

}
