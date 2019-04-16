package GUI.Dialog;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class LoadDialog {

    static Stage stage;

    public static void show(Window owner) {
        stage = null;
        Platform.runLater(() -> {
            stage = new Stage();
//            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
            try {
                FXMLLoader loader = new FXMLLoader(LoadDialog.class.getClass().getResource("/GUI/layout/load.fxml"));
                AnchorPane pane = loader.load();
                stage.setScene(new Scene(pane, 500, 180));
                stage.show();
                stage.setResizable(false);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void close() {
        System.out.println("LoadDialog.close "+stage);
        if (stage != null) {
            while (!stage.isShowing())
                System.out.println("waiting..");
            Platform.runLater(() -> {
                stage.close();
            });

        }
    }
}
