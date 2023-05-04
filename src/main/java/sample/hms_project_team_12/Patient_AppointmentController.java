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
import sample.hms_project_team_12.MedicalRecord.MedicalRecord;
import sample.hms_project_team_12.User.Doctor;
import sample.hms_project_team_12.User.Patient;
import sample.hms_project_team_12.User.User;
import sample.hms_project_team_12.database.DataBaseConnection;
import sample.hms_project_team_12.util.Auth;
import sample.hms_project_team_12.util.CheckboxPopup;
import sample.hms_project_team_12.util.PatientControllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Patient_AppointmentController implements Initializable {

    private static int patient_id;

    @FXML
    private Button btn_myDetails;
    @FXML
    private Button btn_appointment;
    @FXML
    private Button btn_logout;

    // Tabs
    @FXML
    private Tab tb_newAppointment;
    @FXML
    private Tab tb_myAppointment;
    // New Appointment
    @FXML
    private TextField tf_searchDoctor;

    // Appointment Table (Select Doctor)
    @FXML
    private TableView<Doctor> table_selectDoctor;
    @FXML
    private TableColumn<Doctor, String> tcNA_doctorName;
    @FXML
    private TableColumn<Doctor, String> tcNA_specialty;
    @FXML
    private TableColumn<Doctor, Void> tcNA_more;

    //
    @FXML
    private Label label_doctorName;
    @FXML
    private Label label_specialty;

    // Doctor Availability table
    @FXML
    private TableView<DoctorAvailability> table_booking;
    @FXML
    private TableColumn<DoctorAvailability, String> tcNA_date;
    @FXML
    private TableColumn<DoctorAvailability, String> tcNA_time;
    @FXML
    private TableColumn<DoctorAvailability, Void> tcNA_book;


    // Appointment Table
    @FXML
    private TableView<Appointment> table_appointment;
    @FXML
    private TableColumn<Appointment, Integer> tc_id;
    @FXML
    private TableColumn<Appointment, String> tc_doctorName;
    @FXML
    private TableColumn<Appointment, String> tc_AppointmentDate;
    @FXML
    private TableColumn<Appointment, String> tc_AppointmentTime;
    @FXML
    private TableColumn<Appointment, String> tc_status;

    // Appointments Search
    @FXML
    private TextField tf_search;
    @FXML
    private Button btn_filter;


    // For Filter Method
    CheckboxPopup checkboxPopup = new CheckboxPopup();
    String[] checkboxNames = {"ID", "Doctor Name", "Appointment Date", "Status"};
    boolean[] checkBoxStates = {true, true, true, true};

    // Set Patient ID
    public static void setId(int id) {
        patient_id = id;
    }

    // Load Doctor Table (table_selectDoctor)
    public void loadDoctorTableData(){

        ObservableList doctorList  = Doctor.getDoctorList();

        try {
            tcNA_doctorName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            tcNA_specialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));
            tcNA_more.setCellValueFactory(null);

            // MORE Button
            tcNA_more.setCellFactory(column -> new TableCell<Doctor, Void>() {
                private final Button moreButton = new Button("More");

                {
                    moreButton.setOnAction(event -> {
                        Doctor doctorSearch = getTableView().getItems().get(getIndex());
                        int doc_id = doctorSearch.getUser_id();
                        try {
                            loadDoctorAvailabilityTableData(doc_id);
                            label_doctorName.setText("Dr. " + doctorSearch.getFullName());
                            label_specialty.setText(doctorSearch.getSpecialty());
                        } catch (Exception e) {
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
            table_selectDoctor.setItems(doctorList);

            // Initial filtered list
            FilteredList<Doctor> filteredData = new FilteredList<>(doctorList, b -> true);

            tf_searchDoctor.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(doctorSearchByFullName -> {

                    // if no search value then display all records or whatever record currently have. no changes
                    if(newValue.isEmpty() || newValue.isBlank() ){
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if(doctorSearchByFullName.getFullName().toLowerCase().indexOf(searchKeyword) > -1){
                        return true; // Means we found a match in results
                    } if(doctorSearchByFullName.getSpecialty().toLowerCase().indexOf(searchKeyword) > -1){
                        return true; // Means we found a match in results
                    } else {
                        return false; // No match found
                    }
                });
            });

            SortedList<Doctor> sortedData = new SortedList<>(filteredData);

            // bind sorted data with table view
            sortedData.comparatorProperty().bind(table_selectDoctor.comparatorProperty());

            // apply filtered and sorted data to the table view
            table_selectDoctor.setItems(sortedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load Booking Table (table_booking)
    public void loadDoctorAvailabilityTableData(int doc_id) {
        ObservableList doctorAvailabilityObservableList = (ObservableList) DoctorAvailability.getDoctorAvailabilityListByDoctorId(doc_id);
        try {
            tcNA_date.setCellValueFactory(new PropertyValueFactory<>("date"));
            tcNA_time.setCellValueFactory(new PropertyValueFactory<>("time"));
            tcNA_book.setCellValueFactory(null);

            // MORE Button
            tcNA_book.setCellFactory(column -> new TableCell<DoctorAvailability, Void>() {
                private final Button bookButton = new Button("Book");
                {
                    bookButton.setText("Book");
                    bookButton.setStyle("-fx-background-color: #2B3467; -fx-text-fill: #fff; -fx-cursor: HAND");

                    bookButton.setOnAction(event -> {
                        DoctorAvailability doctorAvailabilitySearch = getTableView().getItems().get(getIndex());
                        try {
                            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                            confirm.setTitle("Confirmation");
                            confirm.setHeaderText("Are you sure you want to book this appointment?");
                            confirm.setContentText("Doctor: " + Doctor.getNameById(doctorAvailabilitySearch.getDoctor_id()) +
                                    "\nDate: " + doctorAvailabilitySearch.getDate() +
                                    "\nTime: " + doctorAvailabilitySearch.getTime());
                            Optional<ButtonType> result = confirm.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                AppointmentSQL.createNewAppointment(new Appointment(doc_id, patient_id, doctorAvailabilitySearch.getDate(), doctorAvailabilitySearch.getTime(), Appointment.StatusTypes.SCHEDULED));
                                bookButton.setText("Booked");
                                bookButton.setStyle("-fx-background-color: #eededed; -fx-text-fill: #000; -fx-cursor: HAND");
                            }
                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Error booking appointment");
                            alert.setContentText("An error occurred while booking the appointment. Please try again later.");
                            alert.showAndWait();
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
                        setGraphic(bookButton);
                    }
                }

            });

            table_booking.setItems(doctorAvailabilityObservableList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // load data to Appointment Table
    public void loadAppointmentTable() {
        ObservableList AppointmentObservableList = AppointmentSQL.getAppointmentByPatientID(patient_id);

        try {
            tc_id.setCellValueFactory(new PropertyValueFactory<>("appointment_id"));
            tc_doctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
            tc_AppointmentDate.setCellValueFactory(new PropertyValueFactory<>("appointment_date"));
            tc_AppointmentTime.setCellValueFactory(new PropertyValueFactory<>("appointment_time"));
            tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));

            table_selectDoctor.setItems(AppointmentObservableList);


            // Initial filtered list
            FilteredList<Appointment> filteredData = new FilteredList<>(AppointmentObservableList, b -> true);

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

                    if(Integer.toString(appointmentSearchModule.getAppointment_id()).toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[0]){
                        return true; // Means we found a match in results
                    } else if(appointmentSearchModule.getDoctorName().toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[1]) {
                        return true;
                    } else if(appointmentSearchModule.getAppointment_date().toString().toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[2]) {
                        return true;
                    } else if(appointmentSearchModule.getStatus().toString().toLowerCase().indexOf(searchKeyword) > -1 && checkBoxStates[3]) {
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

        // Load Data to Appointments
        loadAppointmentTable();

        // Load Doctors data
        loadDoctorTableData();

        // Search Filter
        btn_filter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkBoxStates = checkboxPopup.showPopupAndGetCheckboxStates(checkboxNames, checkBoxStates, btn_filter);
            }
        });

        tb_newAppointment.selectedProperty().addListener((observable, oldValue, newValue) -> {
            // Load Doctors data
            loadDoctorTableData();
        });
        tb_myAppointment.selectedProperty().addListener((observable, oldValue, newValue) -> {
            // Load Data to Appointments
            loadAppointmentTable();
        });

        // change scenes
        PatientControllers patientControllers = new PatientControllers();
        patientControllers.appointmentsButton(btn_appointment);
        patientControllers.myDetailsButton(btn_myDetails);

        // logout
        Auth auth = new Auth();
        auth.onClickLogoutButton(btn_logout);
    }
}

