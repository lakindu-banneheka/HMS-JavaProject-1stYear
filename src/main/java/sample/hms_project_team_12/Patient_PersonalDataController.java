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
import sample.hms_project_team_12.MedicalRecord.MedicalRecord;
import sample.hms_project_team_12.User.Doctor;
import sample.hms_project_team_12.User.Patient;
import sample.hms_project_team_12.User.User;
import sample.hms_project_team_12.util.Auth;
import sample.hms_project_team_12.util.CheckboxPopup;
import sample.hms_project_team_12.util.DoctorControllers;
import sample.hms_project_team_12.util.PatientControllers;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Patient_PersonalDataController implements Initializable {

    private static int patient_id;

    @FXML
    private Button btn_myDetails;
    @FXML
    private Button btn_appointment;
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
    private Button button_editPersonalDetails;

    @FXML
    private Button button_submitEditedPersonalDetails;

    // Record Table IDs
    @FXML
    private TableView<MedicalRecord> table_record;
    @FXML
    private TableColumn<MedicalRecord, Integer> tc_id;
    @FXML
    private TableColumn<MedicalRecord, String> tc_doctorName;
    @FXML
    private TableColumn<MedicalRecord, String> tc_symptoms;
    @FXML
    private TableColumn<MedicalRecord, String> tc_email;
    @FXML
    private TableColumn<MedicalRecord, String> tc_diagnoses;
    @FXML
    private TableColumn<MedicalRecord, String> tc_treatments;
    @FXML
    private TableColumn<MedicalRecord, String> tc_notes;
    @FXML
    private TableColumn<MedicalRecord, String> tc_date;

    @FXML
    private TextField tf_search;
    @FXML
    private Button btn_filter;


    // Search Function Variables
    ObservableList<MedicalRecord> recordSearchModuleObservableList = FXCollections.observableArrayList();

    // For Filter Method
    CheckboxPopup checkboxPopup = new CheckboxPopup();
    String[] checkboxNames = {"id", "doctor name"};
    boolean[] checkBoxStates = {true, true, true, true};

    String[] genderArray = {User.Gender.FEMALE.name(), User.Gender.MALE.name()};

    // Set Patient ID
    public static void setId(int id) {
        patient_id = id;
    }

    // Get data From Records
    private void getDataFromRecords() {

        // get record data by patient ID
        MedicalRecord medicalRecord = new MedicalRecord();
        recordSearchModuleObservableList = medicalRecord.getMedicalDataOfPatientById(patient_id);

        // Get Doctor name
        for (MedicalRecord record : recordSearchModuleObservableList) {
            String doctorName = Doctor.getNameById(record.getDoctor_id());
            record.setDoctorName(doctorName);
        }

        try {
            // PropertyValueFactory corresponds to the new AdminSearchModule fields
            tc_id.setCellValueFactory(new PropertyValueFactory<>("record_id"));
            tc_doctorName.setCellValueFactory(new PropertyValueFactory<>("doctor_name"));
            tc_symptoms.setCellValueFactory(new PropertyValueFactory<>("symptoms"));
            tc_diagnoses.setCellValueFactory(new PropertyValueFactory<>("diagnoses"));
            tc_treatments.setCellValueFactory(new PropertyValueFactory<>("treatments"));
            tc_notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
            tc_date.setCellValueFactory(new PropertyValueFactory<>("date"));

            table_record.setItems(recordSearchModuleObservableList);

            // Initial filtered list
            FilteredList<MedicalRecord> filteredData = new FilteredList<>(recordSearchModuleObservableList, b -> true);

            tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(searchModule -> {

                    // if no search value then display all records or whatever record currently have. no changes
                    if(newValue.isEmpty() || newValue.isBlank()){
                        return true;
                    }
                    boolean allFalse = true;

                    for (boolean state : checkBoxStates) {
                        if (state) {
                            allFalse = false;
                            break;
                        }
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if(Integer.toString(searchModule.getRecord_id()).toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[3]){
                        return true; // Means we found a match in results
                    }
                    if(Integer.toString(searchModule.getDoctor_id()).toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[1]) {
                        return true;
                    }
//                    if(searchModule.getSymptoms().toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[2]) {
//                        return true;
//                    }
//                    if(searchModule.getDiagnoses().toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[0]) {
//                        return true;
//                    }
//                    if(searchModule.getTreatments().toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[0]) {
//                        return true;
//                    }

                    // No match found
                    if(allFalse){
                        System.out.println("Select search filters by checking the checkboxes.");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Select search filters by checking the checkboxes.");
                        alert.show();
                    }
                    return false;
                });
            });

            SortedList<MedicalRecord> sortedData = new SortedList<>(filteredData);

            // bind sorted data with table view
            sortedData.comparatorProperty().bind(table_record.comparatorProperty());

            // apply filtered and sorted data to the table view
            table_record.setItems(sortedData);


        } catch (Exception e) {
            Logger.getLogger(Admin_DoctorListController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

        // Search Filter
        btn_filter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkBoxStates = checkboxPopup.showPopupAndGetCheckboxStates(checkboxNames, checkBoxStates, btn_filter);
            }
        });

    }

    // Disable Submit Buttons
    private void disableSubmitButtons() {
        // Disable submit buttons
        button_submitEditedPersonalDetails.setDisable(true);
        button_submitEditedPersonalDetails.setText("");
        button_submitEditedPersonalDetails.setStyle("-fx-background-color: transparent;");

        // Enable button_editPersonalDetails button
        button_editPersonalDetails.setDisable(false);
        button_editPersonalDetails.setText("Edit Personal Data");
        button_editPersonalDetails.setStyle("-fx-background-color: #2B3467;");

    }

    // Update Edited Doctor Data
    private void updatePatient() {
        User patient = new Patient(patient_id ,tf_firstname.getText(),tf_lastname.getText(), tf_address.getText(), tf_phone.getText(), tf_email.getText(), User.Gender.valueOf(cb_gender.getValue()), Date.valueOf(d_dob.getValue()), tf_nic.getText());
        patient.updateUserDataById(patient, patient_id);
    }

    // Set Doctor Data to TextFields
    private void setPatientData() {
        try {
                User patient = new Patient();
                User retrievePatient = patient.getUserDataById(patient_id);

                // set data to fields
                tf_firstname.setText(retrievePatient.getFirstname());
                tf_lastname.setText(retrievePatient.getLastname());
                tf_phone.setText(retrievePatient.getPhone());
                tf_email.setText(retrievePatient.getEmail());
                tf_address.setText(retrievePatient.getAddress());
                tf_nic.setText(retrievePatient.getNic());
                cb_gender.setValue(retrievePatient.getGender().toString());
                d_dob.setValue(retrievePatient.getDateOfBirth().toLocalDate());

            // disable fields
            disableFields();

        } catch (Exception e) {
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

        cb_gender.setStyle("-fx-opacity: 1.0; -fx-text-fill: #000; -fx-background-color: #fff; -fx-font-family: arial; -fx-font-size: 18px; -fx-font-weight: 600;");
        d_dob.setStyle("-fx-opacity: 1.0; -fx-text-fill: #000; -fx-background-color: #fff; -fx-font-family: arial; -fx-font-size: 18px; -fx-font-weight: 600;");

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Disable submit button
        disableSubmitButtons();

        // Autofill the field with database Data
        setPatientData();

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

                // on click submit button
                updatePatient();
                // set all fields disable
                disableFields();
                // setDisable submit Button
                disableSubmitButtons();

                // Enable button_editPersonalDetails button
                button_editPersonalDetails.setDisable(false);
                button_editPersonalDetails.setText("Edit Personal Data");
                button_editPersonalDetails.setStyle("-fx-background-color: #2B3467;");

                setPatientData();
            }
        });


        // get Data from record
        getDataFromRecords();

        // change scenes
        PatientControllers patientControllers = new PatientControllers();
        patientControllers.appointmentsButton(btn_appointment);
        patientControllers.myDetailsButton(btn_myDetails);

        // logout
        Auth auth = new Auth();
        auth.onClickLogoutButton(btn_logout);
    }


}

