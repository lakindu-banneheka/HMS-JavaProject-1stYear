package sample.hms_project_team_12.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import sample.hms_project_team_12.*;
import sample.hms_project_team_12.User.Doctor;
import sample.hms_project_team_12.User.Patient;
import sample.hms_project_team_12.User.User;
import sample.hms_project_team_12.database.DataBaseConnection;
import java.io.IOException;
import java.sql.*;

public class Auth {


    // --- User login ---

    // connection with database (check email from DB)
    private static boolean checkEmailAndPasswordWithDB(String email, String password, String checkUserExistsQuery) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        try {
            preparedStatement = connectDB.prepareStatement(checkUserExistsQuery);
            preparedStatement.setString(1,email);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                System.out.println("email not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
                return false;
            } else {
                while (resultSet.next()) {
                    String retrievePassword = resultSet.getString("password");

                    if(retrievePassword.equals(password)){
                        System.out.println("login - email-password match");
                        return true;
                    } else {
                        System.out.println("password did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are incorrect!");
                        alert.show();
                        return false;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    // User login - (patient, doctor, admin)
    public static void userLogin(ActionEvent event, String email, String password, User.AccountType accountType) {

        if(email.contains("@")){
            // SQL Query - Executed in the backend database
            String Admin_checkUserExistsQuery = "SELECT password FROM admin WHERE email = ?";
            String Doctor_checkUserExistsQuery = "SELECT password FROM doctor WHERE email = ?";
            String Patient_checkUserExistsQuery = "SELECT password FROM patient WHERE email = ?";

            switch (accountType) {
                case ADMIN:
                    if (checkEmailAndPasswordWithDB(email, password, Admin_checkUserExistsQuery)) {
                        try {
                            SceneController.switchScene(event,"admin_doctor-register-view.fxml","ABC Hospital - Admin");

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case DOCTOR:
                    if (checkEmailAndPasswordWithDB(email, password, Doctor_checkUserExistsQuery)) {
                        try {
                            int doc_id = Doctor.getIdByEmail(email);
                            Doctor_PersonalDetailsController.setDoctor_id(doc_id);
                            Doctor_SearchPatientsController.setDoctor_id(doc_id);
                            Doctor_DoctorAvailabilityListController.setDoctor_id(doc_id);
                            SceneController.switchScene(event,"doctor_personal-details-view.fxml","ABC Hospital - Doctor");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case PATIENT:
                    if (checkEmailAndPasswordWithDB(email, password, Patient_checkUserExistsQuery)) {
                        try {
                            int patient_id = Patient.getIdByEmail(email);
                            Patient_PersonalDataController.setId(patient_id);
                            Patient_AppointmentController.setId(patient_id);
                            SceneController.switchScene(event,"patient_personal-details-view.fxml","ABC Hospital - Patient");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
            }
        } else {
            System.out.println("Invalid email");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Provided credentials are incorrect!");
            alert.show();
        }
    }

    // --- Logout ---
    public void onClickLogoutButton(Button btn_logout) {
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

    // --- Register ---

    // Doctor Register - connection with database
    public static void registerDoctor(ActionEvent event, Doctor doctor, String password){
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String checkUserExistsQuery = "SELECT * FROM doctor WHERE email = ?";
        String doctorRegisterQuery = "INSERT INTO doctor (firstname, lastname, address, phone, email, gender, password, dob, nic, specialty, licenseNumber  ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            if(ErrorChecking.isValidEmail(doctor.getEmail())) {
                psCheckUserExists = connectDB.prepareStatement(checkUserExistsQuery);
                psCheckUserExists.setString(1, doctor.getEmail());
                resultSet = psCheckUserExists.executeQuery();


                if (resultSet.isBeforeFirst()) {
                    System.out.println("User already exists!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You cannot use this Email!");
                    alert.show();
                } else {
                    psInsert = connectDB.prepareStatement(doctorRegisterQuery);
                    psInsert.setString(1, doctor.getFirstname());
                    psInsert.setString(2, doctor.getLastname());
                    psInsert.setString(3, doctor.getAddress());
                    psInsert.setString(4, doctor.getPhone());
                    psInsert.setString(5, doctor.getEmail());
                    psInsert.setString(6, String.valueOf(doctor.getGender()));
                    psInsert.setString(7, password);
                    psInsert.setDate(8, doctor.getDateOfBirth());
                    psInsert.setString(9, doctor.getNic());
                    psInsert.setString(10, doctor.getSpecialty());
                    psInsert.setString(11, doctor.getLicenseNumber());

                    int r = psInsert.executeUpdate();

                    if(r > 0){
                        System.out.println("Registered as a Doctor");

                        System.out.println("Successfully Registered.");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Successfully Registered!");
                        alert.show();
                    } else {
                        System.out.println("Error while registering");

                        System.out.println("Error while registering.");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Error while registering!");
                        alert.show();
                    }
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (resultSet != null ){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psCheckUserExists != null ){
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psInsert != null ){
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Patient Register - connection with database
    public static void registerPatient(ActionEvent event, Patient patient, String password){
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String checkUserExistsQuery = "SELECT * FROM patient WHERE email = ?";
        String patientRegisterQuery = "INSERT INTO patient (firstname, lastname, address, phone, email, gender, password, dob, nic) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try {
            if(ErrorChecking.isValidEmail(patient.getEmail())){
                psCheckUserExists = connectDB.prepareStatement(checkUserExistsQuery);
                psCheckUserExists.setString(1, patient.getEmail());
                resultSet = psCheckUserExists.executeQuery();

                if (resultSet.isBeforeFirst()) {
                    System.out.println("User already exists!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You cannot use this Email!");
                    alert.show();
                } else {
                    psInsert = connectDB.prepareStatement(patientRegisterQuery);
                    psInsert.setString(1, patient.getFirstname());
                    psInsert.setString(2, patient.getLastname());
                    psInsert.setString(3, patient.getAddress());
                    psInsert.setString(4, patient.getPhone());
                    psInsert.setString(5, patient.getEmail());
                    psInsert.setString(6, String.valueOf(patient.getGender()));
                    psInsert.setString(7, password);
                    psInsert.setDate(8, patient.getDateOfBirth());
                    psInsert.setString(9, patient.getNic());

                    int r = psInsert.executeUpdate();
                    if(r > 0){
                        System.out.println("Registered as a Patient");

                        System.out.println("Successfully Registered.");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Successfully Registered!");
                        alert.show();
                    } else {
                        System.out.println("Error while registering.");

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Error while registering!");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (resultSet != null ){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psCheckUserExists != null ){
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psInsert != null ){
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
