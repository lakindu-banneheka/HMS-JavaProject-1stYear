package sample.hms_project_team_12;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.hms_project_team_12.User.Admin;
import sample.hms_project_team_12.User.Doctor;
import sample.hms_project_team_12.database.AdminSearchModule;
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

public class Admin_AdminListController implements Initializable {

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

    @FXML
    private Button btn_search;


    @FXML
    private TableView<AdminSearchModule> table_admin;

    @FXML
    private TableColumn<AdminSearchModule, Integer> tc_id;
    @FXML
    private TableColumn<AdminSearchModule, String> tc_email;
    @FXML
    private TableColumn<AdminSearchModule, String> tc_password;
    @FXML
    private TableColumn<AdminSearchModule, Void> tc_more;

    @FXML
    private TextField tf_search;

    ObservableList<AdminSearchModule> adminSearchModuleObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String adminViewQuery = "SELECT * FROM hms.admin";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(adminViewQuery);

            while (queryOutput.next()) {
                Integer queryAdminID = queryOutput.getInt("admin_id");
                String queryEmail = queryOutput.getString("email");
                String queryPassword = queryOutput.getString("password");

                // Populate the ObservableList
                adminSearchModuleObservableList.add(new AdminSearchModule(queryAdminID,queryEmail,queryPassword));
            }

            // PropertyValueFactory corresponds to the new AdminSearchModule fields
            tc_id.setCellValueFactory(new PropertyValueFactory<>("admin_id"));
            tc_email.setCellValueFactory(new PropertyValueFactory<>("email"));
            tc_password.setCellValueFactory(new PropertyValueFactory<>("password"));
//            tc_more.setCellValueFactory(null);

            // column More Button
            tc_more.setCellFactory(column -> new TableCell<AdminSearchModule, Void>() {
                private final Button moreButton = new Button("More");

                {
                    moreButton.setOnAction(event -> {
                        AdminSearchModule adminSearchModule = getTableView().getItems().get(getIndex());
                        int adminID = adminSearchModule.getAdmin_id();

                        try {
                            AdminMoreViewController.setId(adminID);
                            SceneController.switchScene(event, "admin_AdminMore-view.fxml", "ABC Hospital - Admin");
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


            table_admin.setItems(adminSearchModuleObservableList);
//            table_admin.getColumns().add(tc_more);

            // Initial filtered list
            FilteredList<AdminSearchModule> filteredList = new FilteredList<>(adminSearchModuleObservableList, b -> true);

            tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(adminSearchModule -> {

                    // if no search value then display all records or whatever record currently have. no changes
                    if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if(adminSearchModule.getEmail().toLowerCase().indexOf(searchKeyword) > -1){
                        return true; // Means we found a match in results
                    } else {
                        return false; // No match found
                    }

                });
            });

            SortedList<AdminSearchModule> sortedData = new SortedList<>(filteredList);

            // bind sorted data with table view
            sortedData.comparatorProperty().bind(table_admin.comparatorProperty());

            // apply filtered and sorted data to the table view
            table_admin.setItems(sortedData);

        } catch (SQLException e) {
            Logger.getLogger(Admin_AdminListController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }



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

