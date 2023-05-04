package sample.hms_project_team_12;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.hms_project_team_12.User.Patient;
import sample.hms_project_team_12.User.User;
import sample.hms_project_team_12.util.Auth;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SelfRegisterPatientController implements Initializable {

    @FXML
    private Button button_login;
    @FXML
    private Button button_register;
    @FXML
    private TextField tf_firstname;
    @FXML
    private TextField tf_lastname;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_phone;
    @FXML
    private DatePicker d_dateofbirth;
    @FXML
    private TextField tf_nic;
    @FXML
    private TextField tf_address;
    @FXML
    private ComboBox<String> cb_gender;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_com_password;

    String[] genderArray = {User.Gender.FEMALE.name(), User.Gender.MALE.name()};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cb_gender.getItems().addAll(genderArray);
        cb_gender.setValue(genderArray[0]);
        d_dateofbirth.setValue(LocalDate.parse("2000-01-01"));

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("@RegisterController on click login button");
                try {
                    SceneController.switchScene(event,"hello-view.fxml","ABC Hospital - Login");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        button_register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!tf_email.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()){
                    if(tf_password.getText().equals(tf_com_password.getText())){
                        Date dateOfBirthSQL_Date = Date.valueOf(d_dateofbirth.getValue() != null ? d_dateofbirth.getValue().toString() : "1900-01-01");
                        Patient patient = new Patient(tf_firstname.getText(), tf_lastname.getText(), tf_address.getText(), tf_phone.getText(), tf_email.getText(), cb_gender.getValue(), dateOfBirthSQL_Date, tf_nic.getText(), User.AccountType.PATIENT);
                        Auth.registerPatient(event,patient,tf_password.getText());
                    } else {
                        System.out.println("Please Re-enter the same password!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Please Re-enter the same password!");
                        alert.show();
                    }
                } else {
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill email and password fields to Register!");
                    alert.show();
                }
            }
        });
    }
}
