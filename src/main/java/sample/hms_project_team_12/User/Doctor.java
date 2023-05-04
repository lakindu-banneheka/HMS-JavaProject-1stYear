package sample.hms_project_team_12.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.hms_project_team_12.MedicalRecord.MedicalRecord;
import sample.hms_project_team_12.database.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class Doctor extends User {
    private String specialty;
    private String licenseNumber;
    private String fullName;

    private static AccountType accountType = AccountType.DOCTOR;
    public Doctor(String email) {
        super(email);
    }

    @Override
    public User getUserDataById(int user_id) {
        return null;
    }

    @Override
    public void updateUserDataById(User user,int userId) {

    }

    @Override
    public void createNewUserData() {

    }

    public Doctor(String firstname, String lastname, String address, String phone, String email, String gender, Date dateOfBirth, String nic, AccountType accountType, String specialty, String licenseNumber) {
        super(firstname, lastname, address, phone, email, gender, dateOfBirth, nic, accountType);
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
    }

    public Doctor(int user_id, String firstname, String lastname, String address, String phone, String email, String gender, Date dateOfBirth, String nic, AccountType accountType, String specialty, String licenseNumber) {
        super(user_id, firstname, lastname, address, phone, email, gender, dateOfBirth, nic, accountType);
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
    }

    public Doctor(int user_id, String firstname, String lastname, String email, String phone, String specialty, String gender, String licenseNumber){
        super(user_id, firstname, lastname, phone, email, gender);
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
    }

    public Doctor(int user_id, String firstname, String lastname, String specialty) {
        super(user_id, firstname, lastname);
        this.specialty = specialty;
    }

    // getter
    public String getSpecialty() {
        return specialty;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getFullName() {
        return super.getFirstname() + " " + super.getLastname();
    }


    // Setter
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Doctor get details By Id
    public static String getNameById (int id){
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String getDoctorNameByIDQuery = "SELECT * FROM hms.doctor WHERE user_id = ?";
        try {
            preparedStatement = connectDB.prepareStatement(getDoctorNameByIDQuery);
            preparedStatement.setInt(1, id);
            queryOutput = preparedStatement.executeQuery();

            if (queryOutput.next()) {
                String queryFirstName = queryOutput.getString("firstname");
                String queryLastName = queryOutput.getString("lastname");

                return queryFirstName + " " + queryLastName;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (queryOutput != null) {
                try {
                    queryOutput.close();
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

        return null;
    }

    // Get ID by Email - Doctor
    public static int getIdByEmail(String email) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String getIdExistsQuery = "SELECT user_id FROM doctor WHERE email = ?";

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        try {
            preparedStatement = connectDB.prepareStatement(getIdExistsQuery);
            preparedStatement.setString(1,email);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                System.out.println("Email not found in the database!");
            } else {
                while (resultSet.next()) {
                    int retrieveId = resultSet.getInt("user_id");
                    return retrieveId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ObservableList<Doctor> getDoctorList() {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;
        String getDataQuery = "SELECT user_id, firstname, lastname, specialty FROM doctor";

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        try {
            preparedStatement = connectDB.prepareStatement(getDataQuery);
//            preparedStatement.setString(1,"test");
            queryOutput = preparedStatement.executeQuery();

            while (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("user_id");
                String queryFirstName = queryOutput.getString("firstname");
                String queryLastName = queryOutput.getString("lastname");
                String querySpecialty = queryOutput.getString("specialty");
                Doctor newDoctor = new Doctor(queryID, queryFirstName, queryLastName, querySpecialty);
                newDoctor.setFullName(queryFirstName + " " + queryLastName);
                doctors.add(newDoctor);
            }
            return doctors;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (queryOutput != null) {
                    queryOutput.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return doctors;
    }


}
