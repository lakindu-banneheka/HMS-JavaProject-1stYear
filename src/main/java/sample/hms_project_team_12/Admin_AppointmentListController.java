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
import sample.hms_project_team_12.database.DataBaseConnection;
import sample.hms_project_team_12.util.AdminControllers;
import sample.hms_project_team_12.util.Auth;
import sample.hms_project_team_12.util.CheckboxPopup;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Admin_AppointmentListController implements Initializable {

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
    private TableView<Appointment> table_appointment;
    @FXML
    private TableColumn<Appointment, Integer> tc_id;
    @FXML
    private TableColumn<Appointment, String> tc_doctorName;
    @FXML
    private TableColumn<Appointment, String> tc_patientName;
    @FXML
    private TableColumn<Appointment, String> tc_date;
    @FXML
    private TableColumn<Appointment, String> tc_time;
    @FXML
    private TableColumn<Appointment, String> tc_status;
    @FXML
    private TableColumn<Appointment, Void> tc_edit;
    @FXML
    private TextField tf_search;
    @FXML
    private Button btn_filter;

    // For Filter Method
    CheckboxPopup checkboxPopup = new CheckboxPopup();
    String[] checkboxNames = {"ID", "Doctor Name", "Patient Name", "Date", "Status"};
    boolean[] checkBoxStates = {true, true, true, true, true};

    public void loadAppointmentTableData() {
        ObservableList<Appointment> appointmentObservableList = AppointmentSQL.getAllAppointmentList();
        try {
            tc_id.setCellValueFactory((new PropertyValueFactory<>("appointment_id")));
            tc_doctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
            tc_patientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
            tc_date.setCellValueFactory(new PropertyValueFactory<>("appointment_date"));
            tc_time.setCellValueFactory(new PropertyValueFactory<>("appointment_time"));
            tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));

            tc_edit.setCellValueFactory(null);

            // MORE Button
            tc_edit.setCellFactory(column -> new TableCell<Appointment, Void>() {
                private final Button editButton = new Button("Edit");
                {
                    editButton.setText("Edit");
                    editButton.setStyle("-fx-background-color: #2B3467; -fx-text-fill: #fff; -fx-cursor: HAND");

                    editButton.setOnAction(event -> {
                        Appointment appointment = getTableView().getItems().get(getIndex());
                        CheckboxPopup.editAppointmentInPopUp(appointment);
                        loadAppointmentTableData();
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

            table_appointment.setItems(appointmentObservableList);

            // Initial filtered list
            FilteredList<Appointment> filteredData = new FilteredList<>(appointmentObservableList, b -> true);

            tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(appointmentSearchModule -> {

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

                    if( checkBoxStates[0] && Integer.toString(appointmentSearchModule.getAppointment_id()).toLowerCase().indexOf(searchKeyword) > -1 ){
                        return true; // Means we found a match in results
                    }
                    if( checkBoxStates[1] && appointmentSearchModule.getDoctorName() != null && appointmentSearchModule.getDoctorName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }
                    if( checkBoxStates[2] && appointmentSearchModule.getPatientName() != null && appointmentSearchModule.getPatientName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }
                    if( checkBoxStates[3] && appointmentSearchModule.getAppointment_date().toString().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }
                    if( checkBoxStates[4] && appointmentSearchModule.getStatus().toString().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }
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

            SortedList<Appointment> sortedData = new SortedList<>(filteredData);

            // bind sorted data with table view
            sortedData.comparatorProperty().bind(table_appointment.comparatorProperty());

            // apply filtered and sorted data to the table view
            table_appointment.setItems(sortedData);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // load appointment data
        loadAppointmentTableData();

        // Search Filter
        btn_filter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkBoxStates = checkboxPopup.showPopupAndGetCheckboxStates(checkboxNames, checkBoxStates, btn_filter);
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

