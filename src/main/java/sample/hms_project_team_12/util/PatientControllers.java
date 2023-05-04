package sample.hms_project_team_12.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import sample.hms_project_team_12.SceneController;

import java.io.IOException;

public class PatientControllers {


    // my detail scene - patient
    public void myDetailsButton(Button btn_myDetails) {
        btn_myDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.switchScene(event,"patient_personal-details-view.fxml","ABC Hospital - Patient");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // get appointment - patient
    public void appointmentsButton(Button btn_appointmentList) {
        btn_appointmentList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.switchScene(event,"patient_appointment-view.fxml","ABC Hospital - Patient");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
