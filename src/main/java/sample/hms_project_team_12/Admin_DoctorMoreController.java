package sample.hms_project_team_12;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.hms_project_team_12.Appointment.Appointment;
import sample.hms_project_team_12.Appointment.AppointmentSQL;
import sample.hms_project_team_12.Appointment.DoctorAvailability;
import sample.hms_project_team_12.User.Doctor;
import sample.hms_project_team_12.User.User;
import sample.hms_project_team_12.database.DataBaseConnection;
import sample.hms_project_team_12.util.AdminControllers;
import sample.hms_project_team_12.util.Auth;
import sample.hms_project_team_12.util.CheckboxPopup;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Admin_DoctorMoreController implements Initializable {

    private static int doctor_id;

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

    // Doctor Availability (DA)
    @FXML
    private TextField tfDA_search;
    @FXML
    private Button btn_addAvailability;
    @FXML
    private TableView<DoctorAvailability> table_doctorAvailability;
    @FXML
    private TableColumn<DoctorAvailability, Integer> tcDA_id;
    @FXML
    private TableColumn<DoctorAvailability, String> tcDA_date;
    @FXML
    private TableColumn<DoctorAvailability, String> tcDA_time;
    @FXML
    private TableColumn<DoctorAvailability, String> tcDA_isAvailable;
    @FXML
    private TableColumn<DoctorAvailability, Void> tcDA_edit;

    // set Doctor ID
    public static void setDoctor_id(int doctor_id) {
        Admin_DoctorMoreController.doctor_id = doctor_id;
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

    public void loadDoctorAvailabilityTableData(int doc_id) {
        ObservableList doctorAvailabilityObservableList = (ObservableList) DoctorAvailability.getDoctorAvailabilityListByDoctorId(doc_id);
        try {
            tcDA_id.setCellValueFactory((new PropertyValueFactory<>("id")));
            tcDA_date.setCellValueFactory(new PropertyValueFactory<>("date"));
            tcDA_time.setCellValueFactory(new PropertyValueFactory<>("time"));
            tcDA_isAvailable.setCellValueFactory(new PropertyValueFactory<>("is_available"));

            tcDA_edit.setCellValueFactory(null);

            // MORE Button
            tcDA_edit.setCellFactory(column -> new TableCell<DoctorAvailability, Void>() {
                private final Button editButton = new Button("Edit");
                {
                    editButton.setText("Edit");
                    editButton.setStyle("-fx-background-color: #2B3467; -fx-text-fill: #fff; -fx-cursor: HAND");

                    editButton.setOnAction(event -> {
                        DoctorAvailability doctorAvailability = getTableView().getItems().get(getIndex());
                        CheckboxPopup.editDoctorAvailabilityInPopUp(doctorAvailability);
                        loadDoctorAvailabilityTableData(doctor_id);
                    });

                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }

            });

            table_doctorAvailability.setItems(doctorAvailabilityObservableList);

            // Initial filtered list
            FilteredList<DoctorAvailability> filteredData = new FilteredList<>(doctorAvailabilityObservableList, b -> true);

            tfDA_search.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(doctorSearchModule -> {

                    // if no search value then display all records or whatever record currently have. no changes
                    if(newValue.isEmpty() || newValue.isBlank()){
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if(doctorSearchModule.getDate().toString().toLowerCase().indexOf(searchKeyword) > -1){
                        return true; // Means we found a match in results
                    } else if(doctorSearchModule.getTime().toString().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if(Boolean.toString(doctorSearchModule.getIs_available()).toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if(Integer.toString(doctorSearchModule.getId()).toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false; // No match found
                    }
                });
            });

            SortedList<DoctorAvailability> sortedData = new SortedList<>(filteredData);

            // bind sorted data with table view
            sortedData.comparatorProperty().bind(table_doctorAvailability.comparatorProperty());

            // apply filtered and sorted data to the table view
            table_doctorAvailability.setItems(sortedData);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Disable submit button
        disableSubmitButtons();

        // Autofill the field with database Data
        setDoctorData(doctor_id);
        loadDoctorAvailabilityTableData(doctor_id);

        btn_addAvailability.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CheckboxPopup.addNewDoctorAvailabilityInPopUp(doctor_id);
                loadDoctorAvailabilityTableData(doctor_id);
            }
        });

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

        // Submit edited personal Details
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

        // Edit Professional Details
        button_editProfessionalDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Enable Professional details fields
                tf_speciality.setEditable(true);
                tf_licenseNumber.setEditable(true);
                tf_appointmentYear.setEditable(true);

                // setDisable button_editProfessionalDetails Button
                button_editProfessionalDetails.setDisable(true);
                button_editProfessionalDetails.setText("");
                button_editProfessionalDetails.setStyle("-fx-background-color: transparent;");

                // Enable submit button
                button_submitEditedProfessionalDetails.setDisable(false);
                button_submitEditedProfessionalDetails.setText("Submit");
                button_submitEditedProfessionalDetails.setStyle("-fx-background-color: #2B3467;");

            }
        });

        // Submit edited Professional Details
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

        // change scenes
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

