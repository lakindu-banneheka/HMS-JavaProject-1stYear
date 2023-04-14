package sample.hms_project_team_12.util;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CheckboxPopup extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public boolean[] showPopupAndGetCheckboxStates(String[] checkboxNames, boolean[] checkBoxStates, Button btn_filter) {

        btn_filter.setOnAction(event -> {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Filter By");



            CheckBox[] checkboxes = new CheckBox[checkboxNames.length];

            for (int i = 0; i < checkboxNames.length; i++) {
                checkboxes[i] = new CheckBox(checkboxNames[i]);
                checkboxes[i].setSelected(checkBoxStates[i]);
                VBox.setMargin(checkboxes[i], new Insets(5, 5, 0, 10));
            }


            VBox popupVBox = new VBox(checkboxes);
            popupVBox.setAlignment(Pos.TOP_LEFT);


            Button saveButton = new Button("Save");
            saveButton.setStyle("-fx-border-radius: 5px; -fx-font-size: 20px; -fx-font-weight: 600; -fx-font-family: Arial; -fx-background-color: #ededed; -fx-text-fill: #2B3467; -fx-alignment: left; -fx-padding: 0px 15px;");
            VBox.setMargin(saveButton, new Insets(15));

            VBox innerVBox = new VBox(checkboxes);
            innerVBox.setAlignment(Pos.CENTER_LEFT);
            innerVBox.setPadding(new Insets(10));


            saveButton.setOnAction(saveEvent -> {
                for (int i = 0; i < checkboxNames.length; i++) {
                    checkBoxStates[i] = checkboxes[i].isSelected();
                }
                popup.close();
            });

            saveButton.setStyle("-fx-background-color: #2B3467; -fx-text-fill: #fff; -fx-font-size: 14; -fx-cursor: hand;");
            saveButton.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(saveButton, Priority.ALWAYS);

            VBox outerVBox = new VBox(innerVBox, saveButton);
            outerVBox.setAlignment(Pos.CENTER);
            outerVBox.setSpacing(10);
            outerVBox.setPadding(new Insets(10, 10, 10, 10));

            Scene popupScene = new Scene(outerVBox, 400, 220);
            popup.setScene(popupScene);
            popup.showAndWait();
        });

        return checkBoxStates;
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
