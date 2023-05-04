package sample.hms_project_team_12;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.hms_project_team_12.User.Patient;
import sample.hms_project_team_12.User.User;
import sample.hms_project_team_12.util.AdminControllers;
import sample.hms_project_team_12.util.Auth;
import sample.hms_project_team_12.util.TemporaryPasswordGenerator;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Admin_PatientRegisterController implements Initializable {
    @FXML
    private Button btn_createDoctor;
    @FXML
    private Button btn_createPatient;
    @FXML
    private Button btn_getDoctors;
    @FXML
    private Button btn_getPatients;
    @FXML
    private Button btn_getAssignments;
    @FXML
    private Button btn_logout;

    @FXML
    private Button btn_generatePassword;
    @FXML
    private Button button_register;
    @FXML
    private Button button_reset;
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
    private Label lable_password;


    String[] genderArray = {User.Gender.FEMALE.name(), User.Gender.MALE.name()};

    // Reset
    public void onClickPatientRegisterFieldsReset() {
        tf_firstname.setText("");
        tf_lastname.setText("");
        tf_email.setText("");
        tf_phone.setText("");
        tf_nic.setText("");
        tf_address.setText("");
        d_dateofbirth.setValue(LocalDate.parse("2000-01-01"));
        cb_gender.setValue("");
        lable_password.setText("");
        btn_generatePassword.setText("Generate Password");
        btn_generatePassword.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cb_gender.getItems().addAll(genderArray);
        cb_gender.setValue("");
        d_dateofbirth.setValue(LocalDate.parse("2000-01-01"));


        // Generate password button
        btn_generatePassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!tf_firstname.getText().trim().isEmpty() && !tf_nic.getText().trim().isEmpty()){
                    String temPassword = TemporaryPasswordGenerator.generateTemporaryPassword(tf_firstname.getText().trim(), tf_nic.getText().trim());

                    lable_password.setText(temPassword);
                    lable_password.setPrefWidth(100);
                    btn_generatePassword.setText("RE-Generate Password");
                    btn_generatePassword.setDisable(false);

                } else {
                    System.out.println("Please fill in FIRST-NAME and NIC-NUMBER before generating the password");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in FIRST-NAME and NIC-NUMBER before generating the password!");
                    alert.show();

                    lable_password.setText("");
                    btn_generatePassword.setText("Generate Password");
                    btn_generatePassword.setDisable(false);
                }

            }
        });

        // Rest button
        button_reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onClickPatientRegisterFieldsReset();
            }
        });

        // Register button
        button_register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                boolean isRequiredEmpty = tf_firstname.getText().trim().isEmpty() && tf_nic.getText().trim().isEmpty() && tf_phone.getText().trim().isEmpty() && cb_gender.getValue().isEmpty();
                if(!tf_email.getText().trim().isEmpty() && !lable_password.getText().trim().isEmpty() && !isRequiredEmpty ){
                    Patient newPatient = new Patient(tf_firstname.getText(), tf_lastname.getText(), tf_address.getText(), tf_phone.getText(), tf_email.getText(), cb_gender.getValue(), Date.valueOf(d_dateofbirth.getValue()), tf_nic.getText(), User.AccountType.PATIENT);
                    Auth.registerPatient(event,newPatient,lable_password.getText());

                    onClickPatientRegisterFieldsReset();

                    System.out.println("Successfully Registered.");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Successfully Registered!");
                    alert.show();

                } else {
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill all the required (*) fields to Register!");
                    alert.show();
                }
            }
        });


        // Change scenes
        AdminControllers adminControllers = new AdminControllers();

        adminControllers.createDoctorButton(btn_createDoctor);
        adminControllers.createPatientButton(btn_createPatient);
        adminControllers.getDoctorsButton(btn_getDoctors);
        adminControllers.getPatientButton(btn_getPatients);
        adminControllers.getAppointmentsButton(btn_getAssignments);

        // logout
        Auth auth = new Auth();
        auth.onClickLogoutButton(btn_logout);
    }
}
