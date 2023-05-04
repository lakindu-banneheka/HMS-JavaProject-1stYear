package sample.hms_project_team_12.User;

import sample.hms_project_team_12.Admin_PatientsListController;
import sample.hms_project_team_12.database.DataBaseConnection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Patient extends User {

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

    public Patient(int user_id, String firstname, String lastname, String address, String phone, String email, Gender gender, Date dateOfBirth, String nic) {
        super(user_id, firstname, lastname, address, phone, email, gender, dateOfBirth, nic);
    }

    public Patient() {
        super();
    }

    // SQL - Patient
    @Override
    public User getUserDataById(int user_id) {

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String getPatientDataQuery = "SELECT * FROM patient WHERE user_id = ?";
        try {
            PreparedStatement statement = connectDB.prepareStatement(getPatientDataQuery);
            statement.setInt(1, user_id);
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

                User patient = new Patient(user_id ,queryFirstName,queryLastName, queryAddress, queryPhone, queryEmail, User.Gender.valueOf(queryGender), Date.valueOf(queryDOB.toLocalDate()), queryNIC);
                return patient;

            } else {
                System.out.println("There are no Patient data for this id");
                return null;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    public void updateUserDataById(User user,int userId) {
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String updatePatientDataQuery = "UPDATE patient SET "
                + "firstname = ?, "
                + "lastname = ?, "
                + "phone = ?, "
                + "email = ?, "
                + "address = ?, "
                + "nic = ?, "
                + "gender = ?, "
                + "dob = ? "
                + "WHERE user_id = ?";

        try {
            PreparedStatement statement = connectDB.prepareStatement(updatePatientDataQuery);

            // Set parameters for the update statement
            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getNic());
            statement.setString(7, user.getGender().toString());
            statement.setDate(8, Date.valueOf(user.getDateOfBirth().toLocalDate()));
            statement.setInt(9, userId);


            // Execute the update statement
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Patient data updated successfully");
            } else {
                System.out.println("Error: Patient data update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int getIdByEmail(String email) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String getIdExistsQuery = "SELECT user_id FROM patient WHERE email = ?";

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

    public static String getNameById (int id){
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String getDoctorNameByIDQuery = "SELECT * FROM hms.patient WHERE user_id = ?";
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

    @Override
    public void createNewUserData() {

    }
}
