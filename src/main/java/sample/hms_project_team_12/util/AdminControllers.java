package sample.hms_project_team_12.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import sample.hms_project_team_12.SceneController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminControllers implements Initializable {

    // create doctor scene - admin
    public void createDoctorButton(Button btn_createDoctor) {
        btn_createDoctor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.switchScene(event,"admin_doctor-register-view.fxml","ABC Hospital - Admin");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // create patient scene - admin
    public void createPatientButton(Button btn_createPatient) {
        btn_createPatient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.switchScene(event,"admin_patient-register-view.fxml","ABC Hospital - Admin");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // get doctors scene - admin
    public void getDoctorsButton(Button btn_getDoctors) {
        btn_getDoctors.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.switchScene(event,"admin_doctor-list-view.fxml","ABC Hospital - Admin");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // get patient scene - admin
    public void getPatientButton(Button btn_getPatients) {
        btn_getPatients.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.switchScene(event,"admin_patient-list-view.fxml","ABC Hospital - Admin");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // get patient scene - admin
    public void getAppointmentsButton(Button btn_getAssignments) {
        btn_getAssignments.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.switchScene(event,"admin_appointments-list-view.fxml","ABC Hospital - Admin");
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
