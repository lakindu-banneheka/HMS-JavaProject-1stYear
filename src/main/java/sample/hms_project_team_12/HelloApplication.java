package sample.hms_project_team_12;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("ABC Hospital - Login");

        Image image = new Image("D:\\$ECS\\1 YEAR\\2 SEM\\BECS 12243 - Object Oriented Programming\\BECS 12243 - LAB\\$ Project\\code\\HMS_project_TEAM_12\\src\\main\\resources\\icon.png");
        stage.getIcons().add(image);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}