package sample.hms_project_team_12;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.hms_project_team_12.User.User;
import sample.hms_project_team_12.database.DataBaseConnection;
import sample.hms_project_team_12.util.Auth;
import sample.hms_project_team_12.util.DoctorControllers;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Doctor_PersonalDetailsController implements Initializable {
    private static int doctor_id;

    @FXML
    private Button btn_myDetails;
    @FXML
    private Button btn_searchPatient;
    @FXML
    private Button btn_doctorAvailability;
    @FXML
    private Button btn_logout;

    @FXML
    private TextField tf_firstname;
    @FXML
    private TextField tf_lastname;
    @FXML
    private TextField tf_nic;
    @FXML
    private TextField tf_phone;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_address;
    @FXML
    private ComboBox<String> cb_gender;
    @FXML
    private DatePicker d_dob;

    @FXML
    private TextField tf_speciality;
    @FXML
    private TextField tf_licenseNumber;
    @FXML
    private TextField tf_appointmentYear;

    @FXML
    private Button button_editPersonalDetails;
    @FXML
    private Button button_editProfessionalDetails;

    @FXML
    private Button button_submitEditedPersonalDetails;
    @FXML
    private Button button_submitEditedProfessionalDetails;


    // Doctor ID Setter
    public static void setDoctor_id(int doctor_id) {
        Doctor_PersonalDetailsController.doctor_id = doctor_id;
    }
    String[] genderArray = {User.Gender.FEMALE.name(), User.Gender.MALE.name()};

    // Disable Submit Buttons
    private void disableSubmitButtons() {
        // Disable submit buttons
        button_submitEditedPersonalDetails.setDisable(true);
        button_submitEditedPersonalDetails.setText("");
        button_submitEditedPersonalDetails.setStyle("-fx-background-color: transparent;");

        button_submitEditedProfessionalDetails.setDisable(true);
        button_submitEditedProfessionalDetails.setText("");
        button_submitEditedProfessionalDetails.setStyle("-fx-background-color: transparent;");

        // Enable button_editPersonalDetails button
        button_editPersonalDetails.setDisable(false);
        button_editPersonalDetails.setText("Edit Personal Data");
        button_editPersonalDetails.setStyle("-fx-background-color: #2B3467;");

        button_editProfessionalDetails.setDisable(false);
        button_editProfessionalDetails.setText("Edit Professional Data");
        button_editProfessionalDetails.setStyle("-fx-background-color: #2B3467;");
    }

    // Set Doctor Data to TextFields
    private void setDoctorData(int id) {

        cb_gender.getItems().addAll(genderArray);

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String getDoctorDataQuery = "SELECT * FROM doctor WHERE user_id = ?";

        try {
            PreparedStatement statement = connectDB.prepareStatement(getDoctorDataQuery);
            statement.setInt(1, doctor_id);
            ResultSet queryOutput = statement.executeQuery();

            if(queryOutput.next()){
                String queryFirstName = queryOutput.getString("firstname");
                String queryLastName = queryOutput.getString("lastname");
                String queryPhone = queryOutput.getString("phone");
                String queryEmail = queryOutput.getString("email");
                String queryNIC = queryOutput.getString("nic");
                String queryAddress = queryOutput.getString("address");
                String queryGender = queryOutput.getString("gender");
                Date queryDOB = queryOutput.getDate("dob");
                String querySpecialty = queryOutput.getString("specialty");
                String queryLicenseNumber = queryOutput.getString("licenseNumber");
                String queryAppointmentYear = queryOutput.getString("appointmentYear");


                // set data to fields
                tf_firstname.setText(queryFirstName);
                tf_lastname.setText(queryLastName);
                tf_phone.setText(queryPhone);
                tf_email.setText(queryEmail);
                tf_address.setText(queryAddress);
                tf_nic.setText(queryNIC);
                cb_gender.setValue(queryGender);
                d_dob.setValue(queryDOB.toLocalDate());
                tf_speciality.setText(querySpecialty);
                tf_licenseNumber.setText(queryLicenseNumber);
                tf_appointmentYear.setText(queryAppointmentYear);
            } else {
                System.out.println("There are no doctor data for this ID");
            }

            // disable fields
            disableFields();

        } catch (SQLException e) {
            Logger.getLogger(Admin_PatientsListController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

    }

    // Disable All the textFields
    private void disableFields() {
        // disable fields
        tf_firstname.setEditable(false);
        tf_lastname.setEditable(false);
        tf_phone.setEditable(false);
        tf_email.setEditable(false);
        tf_address.setEditable(false);
        tf_nic.setEditable(false);
        cb_gender.setDisable(true); // disabled
        d_dob.setDisable(true); // disabled
        tf_speciality.setEditable(false);
        tf_licenseNumber.setEditable(false);
        tf_appointmentYear.setEditable(false);
        cb_gender.setStyle("-fx-opacity: 1.0; -fx-text-fill: #000; -fx-background-color: #fff; -fx-font-family: arial; -fx-font-size: 18px; -fx-font-weight: 600;");
        d_dob.setStyle("-fx-opacity: 1.0; -fx-text-fill: #000; -fx-background-color: #fff; -fx-font-family: arial; -fx-font-size: 18px; -fx-font-weight: 600;");

    }

    // Update Edited Doctor Data
    private void updateDoctor() {
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String updateDoctorDataQuery = "UPDATE doctor SET "
                + "firstname = ?, "
                + "lastname = ?, "
                + "phone = ?, "
                + "email = ?, "
                + "address = ?, "
                + "nic = ?, "
                + "gender = ?, "
                + "dob = ?, "
                + "specialty = ?, "
                + "licenseNumber = ?, "
                + "appointmentYear = ? "
                + "WHERE user_id = ?";


        try {
            PreparedStatement statement = connectDB.prepareStatement(updateDoctorDataQuery);

            // Set parameters for the update statement
            statement.setString(1, tf_firstname.getText());
            statement.setString(2, tf_lastname.getText());
            statement.setString(3, tf_phone.getText());
            statement.setString(4, tf_email.getText());
            statement.setString(5, tf_address.getText());
            statement.setString(6, tf_nic.getText());
            statement.setString(7, cb_gender.getValue());
            statement.setDate(8, Date.valueOf(d_dob.getValue()));
            statement.setString(9, tf_speciality.getText());
            statement.setString(10, tf_licenseNumber.getText());
            statement.setInt(11, Integer.parseInt(tf_appointmentYear.getText()));
            statement.setInt(12, doctor_id);


            // Execute the update statement
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Doctor data updated successfully");
            } else {
                System.out.println("Error: Doctor data update failed");
            }
        } catch (SQLException e) {
            Logger.getLogger(Admin_PatientsListController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

    }

    // on Click Edit Personal Data button
    private void onClickSubmitPersonalDataButton() {
        button_submitEditedPersonalDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.println("on click submit button");
                updateDoctor();

                // set all fields disable
                disableFields();
                // setDisable submit Button
                disableSubmitButtons();

                // Enable button_editPersonalDetails button
                button_editPersonalDetails.setDisable(false);
                button_editPersonalDetails.setText("Edit Personal Data");
                button_editPersonalDetails.setStyle("-fx-background-color: #2B3467;");

                setDoctorData(doctor_id);
            }
        });
    }

    // on Click Submit edited Professional Details Button
    private void onClickSubmitProfessionalDetailsButton() {
        button_submitEditedProfessionalDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.println("on click submit button");
                updateDoctor();

                // set all fields disable
                disableFields();
                // setDisable submit Button
                disableSubmitButtons();

                // Enable button_editProfessionalDetails button
                button_editProfessionalDetails.setDisable(false);
                button_editProfessionalDetails.setText("Edit Professional Data");
                button_editProfessionalDetails.setStyle("-fx-background-color: #2B3467;");

                setDoctorData(doctor_id);
            }
        });
    }

    // on Click Submit Editable Personal Button
    private void onClickEditableButton() {
        // Edit personal Details
        button_editPersonalDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Enable personal details fields
                tf_firstname.setEditable(true);
                tf_lastname.setEditable(true);
                tf_phone.setEditable(true);
                tf_email.setEditable(true);
                tf_address.setEditable(true);
                tf_nic.setEditable(true);
                cb_gender.setDisable(false);
                d_dob.setDisable(false);

                // setDisable button_editPersonalDetails Button
                button_editPersonalDetails.setDisable(true);
                button_editPersonalDetails.setText("");
                button_editPersonalDetails.setStyle("-fx-background-color: transparent;");

                // Enable submit button
                button_submitEditedPersonalDetails.setDisable(false);
                button_submitEditedPersonalDetails.setText("Submit");
                button_submitEditedPersonalDetails.setStyle("-fx-background-color: #2B3467;");

            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Disable submit button
        disableSubmitButtons();

        // Autofill the field with database Data
        setDoctorData(doctor_id);

        // on Click Edit Personal Data Button
        onClickSubmitPersonalDataButton();

        // on Click Edit Professional data Button
        onClickSubmitProfessionalDetailsButton();

        // on Click Submit Editable Personal Button
        onClickEditableButton();

        // change scenes
        DoctorControllers doctorControllers = new DoctorControllers();
        doctorControllers.myDetailsButton(btn_myDetails);
        doctorControllers.searchPatientButton(btn_searchPatient);
        doctorControllers.doctorAvailabilityButton(btn_doctorAvailability);

        // logout
        Auth auth = new Auth();
        auth.onClickLogoutButton(btn_logout);

    }



}
