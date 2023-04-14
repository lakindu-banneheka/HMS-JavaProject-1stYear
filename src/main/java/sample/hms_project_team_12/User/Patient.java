package sample.hms_project_team_12.User;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import sample.hms_project_team_12.util.ErrorChecking;

import java.sql.*;

public class Patient extends User {
    private int[] recordIds;
    private int[] appointmentIds;
    private int[] billIds;

    // Patient Constructors
    public Patient(String email) {
        super(email);
    }

    public Patient(String firstname, String lastname, String address, String phone, String email, String gender, Date dateOfBirth, String nic, AccountType accountType) {
        super(firstname, lastname, address, phone, email, gender, dateOfBirth, nic, accountType);
    }

    public Patient(int user_id, String firstname, String lastname, String address, String phone, String email, String gender, Date dateOfBirth, String nic, AccountType accountType) {
        super(user_id, firstname, lastname, address, phone, email, gender, dateOfBirth, nic, accountType);
    }

    public Patient(int queryID, String queryFirstName, String queryLastName, String queryEmail, String queryPhone, String queryGender) {
        super(queryID, queryFirstName, queryLastName, queryEmail, queryPhone, queryGender);
    }


    // Patient Register & connection with database
    public static void registerPatient(ActionEvent event, Patient patient, String password){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        // check how to add this
//        DataBaseConnection connectNow = new DataBaseConnection();
//        Connection connectDB = connectNow.getDBConnection();
//
//        // SQL Query - Executed in the backend database
//        String Query = "SELECT * FROM patient WHERE email = ?";

        try {
            if(ErrorChecking.isValidEmail(patient.email)){
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "123");
                psCheckUserExists = connection.prepareStatement("SELECT * FROM patient WHERE email = ?");
                psCheckUserExists.setString(1, patient.email);
                resultSet = psCheckUserExists.executeQuery();

                if (resultSet.isBeforeFirst()) {
                    System.out.println("User already exists!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You cannot use this Email!");
                    alert.show();
                } else {
                    psInsert = connection.prepareStatement("INSERT INTO patient (firstname, lastname, address, phone, email, gender, password, dob, nic) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    psInsert.setString(1, patient.firstname);
                    psInsert.setString(2, patient.lastname);
                    psInsert.setString(3, patient.address);
                    psInsert.setString(4, patient.phone);
                    psInsert.setString(5, patient.email);
                    psInsert.setString(6, String.valueOf(patient.gender));
                    psInsert.setString(7, password);
                    psInsert.setDate(8, patient.dateOfBirth);
                    psInsert.setString(9, patient.nic);

                    int r = psInsert.executeUpdate();
                    if(r > 0){
                        System.out.println("@DoctorClass registered as a Patient");

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

            if (connection != null ){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
