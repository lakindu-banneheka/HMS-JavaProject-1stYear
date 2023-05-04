package sample.hms_project_team_12;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private static Stage stage;
    private static Scene scene;
    private static Parent root;

    public enum SceneTitle {
        ABC_HOSPITAL__LOGIN,
    }

    public static void switchScene(ActionEvent event, String fxmlFile, String title) throws IOException {

        try {
            root = FXMLLoader.load(SceneController.class.getResource(fxmlFile));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
