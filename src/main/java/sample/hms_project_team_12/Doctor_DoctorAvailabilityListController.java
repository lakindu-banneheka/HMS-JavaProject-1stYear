package sample.hms_project_team_12;

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
import sample.hms_project_team_12.util.AdminControllers;
import sample.hms_project_team_12.util.Auth;
import sample.hms_project_team_12.util.CheckboxPopup;
import sample.hms_project_team_12.util.DoctorControllers;

import java.net.URL;
import java.util.ResourceBundle;

public class Doctor_DoctorAvailabilityListController implements Initializable {

    private static int doctor_id;

    @FXML
    private Button btn_myDetails;
    @FXML
    private Button btn_searchPatient;
    @FXML
    private Button btn_doctorAvailability;
    @FXML
    private Button btn_logout;

    // Table
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


    // set doctor ID
    public static void setDoctor_id(int id) {
        doctor_id = id;
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

        // load appointment data
        loadDoctorAvailabilityTableData(doctor_id);

        // Search Filter
//        btn_filter.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                checkBoxStates = checkboxPopup.showPopupAndGetCheckboxStates(checkboxNames, checkBoxStates, btn_filter);
//            }
//        });

        btn_addAvailability.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CheckboxPopup.addNewDoctorAvailabilityInPopUp(doctor_id);
                loadDoctorAvailabilityTableData(doctor_id);
            }
        });

        // change scenes
        AdminControllers adminControllers = new AdminControllers();
        DoctorControllers doctorControllers = new DoctorControllers();
        doctorControllers.myDetailsButton(btn_myDetails);
        doctorControllers.searchPatientButton(btn_searchPatient);
        doctorControllers.doctorAvailabilityButton(btn_doctorAvailability);

        // logout
        Auth auth = new Auth();
        auth.onClickLogoutButton(btn_logout);
    }
}

