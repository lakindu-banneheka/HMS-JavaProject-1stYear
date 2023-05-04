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
import sample.hms_project_team_12.User.Patient;
import sample.hms_project_team_12.database.DataBaseConnection;
import sample.hms_project_team_12.util.AdminControllers;
import sample.hms_project_team_12.util.Auth;
import sample.hms_project_team_12.util.CheckboxPopup;
import sample.hms_project_team_12.util.DoctorControllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Doctor_SearchPatientsController implements Initializable {

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
    private TableView<Patient> table_patient;
    @FXML
    private TableColumn<Patient, Integer> tc_id;
    @FXML
    private TableColumn<Patient, String> tc_firstname;
    @FXML
    private TableColumn<Patient, String> tc_lastname;
    @FXML
    private TableColumn<Patient, String> tc_email;
    @FXML
    private TableColumn<Patient, String> tc_phone;
    @FXML
    private TableColumn<Patient, String> tc_gender;
    @FXML
    private TableColumn<Patient, Void> tc_more;
    @FXML
    private TextField tf_search;
    @FXML
    private Button btn_filter;

    // set doctor ID
    public static void setDoctor_id(int id) {
        doctor_id = id;
    }

    ObservableList<Patient> patientSearchModuleObservableList = FXCollections.observableArrayList();

    // For Filter Method
    CheckboxPopup checkboxPopup = new CheckboxPopup();
    String[] checkboxNames = {"id", "first name", "last name", "email"};
    boolean[] checkBoxStates = {true, true, true, true};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String adminViewQuery = "SELECT * FROM hms.patient";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(adminViewQuery);

            while (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("user_id");
                String queryFirstName = queryOutput.getString("firstname");
                String queryLastName = queryOutput.getString("lastname");
                String queryPhone = queryOutput.getString("phone");
                String queryEmail = queryOutput.getString("email");
                String queryGender = queryOutput.getString("gender");

                // Populate the ObservableList
                patientSearchModuleObservableList.add(new Patient(queryID, queryFirstName, queryLastName, queryPhone, queryEmail, queryGender));
            }

            // PropertyValueFactory corresponds to the new AdminSearchModule fields
            tc_id.setCellValueFactory(new PropertyValueFactory<>("user_id"));
            tc_firstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            tc_lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            tc_email.setCellValueFactory(new PropertyValueFactory<>("email"));
            tc_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            tc_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
            tc_more.setCellValueFactory(null);

            // MORE Button
            tc_more.setCellFactory(column -> new TableCell<Patient, Void>() {
                private final Button moreButton = new Button("More");
                {
                    moreButton.setText("More");
                    moreButton.setStyle("-fx-background-color: #2B3467; -fx-text-fill: #fff; -fx-cursor: HAND");
                    moreButton.setOnAction(event -> {
                        Patient patientSearch = getTableView().getItems().get(getIndex());
                        int _id = patientSearch.getUser_id();

                        try {
                            Doctor_PatientMoreController.setPatient_id(_id);
                            Doctor_PatientMoreController.setDoctor_id(doctor_id);
                            SceneController.switchScene(event, "doctor_patient-more-view.fxml", "ABC Hospital - Admin");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(moreButton);
                    }
                }
            });

            table_patient.setItems(patientSearchModuleObservableList);


            // Initial filtered list
            FilteredList<Patient> filteredData = new FilteredList<>(patientSearchModuleObservableList, b -> true);

            tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(patientSearchModule -> {

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

                    if(patientSearchModule.getEmail().toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[3]){
                        return true; // Means we found a match in results
                    } else if(patientSearchModule.getFirstname().toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[1]) {
                        return true;
                    } else if(patientSearchModule.getLastname().toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[2]) {
                        return true;
                    } else if(Integer.toString(patientSearchModule.getUser_id()).toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[0]) {
                        return true;
                    } else if(allFalse){
                        System.out.println("Select search filters by checking the checkboxes.");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Select search filters by checking the checkboxes.");
                        alert.show();
                        return false;
                    } else {
                        return false; // No match found
                    }

                });
            });

            SortedList<Patient> sortedData = new SortedList<>(filteredData);

            // bind sorted data with table view
            sortedData.comparatorProperty().bind(table_patient.comparatorProperty());

            // apply filtered and sorted data to the table view
            table_patient.setItems(sortedData);


        } catch (SQLException e) {
            Logger.getLogger(Doctor_SearchPatientsController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }


        // Search Filter
        btn_filter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkBoxStates = checkboxPopup.showPopupAndGetCheckboxStates(checkboxNames, checkBoxStates, btn_filter);
            }
        });

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

