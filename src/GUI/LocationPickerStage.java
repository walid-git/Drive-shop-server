package GUI;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LocationPickerStage  {

    static long node = -1;

    public static long pickLocation(Stage owner) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        StackPane pane = new StackPane();
        Map map = new Map(pane, 20, "default.map", null);
        map.setOnMouseClicked(event -> {
            int click = map.getNode(event.getX(), event.getY());
            if (map.getMap()[click / map.getDimension()][click % map.getDimension()] != Map.Cell.OBSTACLE) {
                node = map.getNode(event.getX(), event.getY());
                stage.close();
            }
        });
        stage.setScene(new Scene(pane, 700, 600));
        stage.showAndWait();
        return node;
    }

}
