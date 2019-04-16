package GUI.Dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PromptDialog  {


    public static boolean prompt(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.CANCEL);
        dialog.headerTextProperty().set(null);
        dialog.showAndWait();
        return dialog.getResult() == ButtonType.YES;
    }

}
