package sample.hms_project_team_12.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import sample.hms_project_team_12.SceneController;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DoctorControllers implements Initializable {

    // my detail scene - doctor
    public void myDetailsButton(Button btn_myDetails) {
        btn_myDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.switchScene(event,"doctor_personal-details-view.fxml","ABC Hospital - Doctor");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // search patient scene - doctor
    public void searchPatientButton(Button btn_searchPatient) {
        btn_searchPatient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.switchScene(event,"doctor_search-patient-view.fxml","ABC Hospital - Doctor");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void doctorAvailabilityButton(Button btn_doctorAvailability) {
        btn_doctorAvailability.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.switchScene(event,"doctor_doctorAvailability-list-view.fxml","ABC Hospital - Doctor");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
