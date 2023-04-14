package sample.hms_project_team_12;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sample.hms_project_team_12.User.Admin;
import sample.hms_project_team_12.database.DataBaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminMoreViewController implements Initializable {

    @FXML
    private Button btn_createDoctor;

    @FXML
    private Button btn_createPatient;

    @FXML
    private Button btn_getDoctors;

    @FXML
    private Button btn_getPatients;
    @FXML
    private Button btn_logout;


    static int admin_id;


    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public static void setId(int id) {
        admin_id = id;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        System.out.println("test ID : " + admin_id);



        //scene builder
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
        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.switchScene(event,"hello-view.fxml","ABC Hospital - login");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
