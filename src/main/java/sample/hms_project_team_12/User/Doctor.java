package sample.hms_project_team_12.User;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import sample.hms_project_team_12.util.ErrorChecking;

import java.sql.*;

public class Doctor extends User {
    String specialty;
    String licenseNumber;

    private static AccountType accountType = AccountType.DOCTOR;
    public Doctor(String email) {
        super(email);
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

    // getter
    public String getSpecialty() {
        return specialty;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    // Setter
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }


    // Doctor Register & connection with database
    public static void registerDoctor(ActionEvent event, Doctor doctor, String password){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            if(ErrorChecking.isValidEmail(doctor.email)) {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "123");
                psCheckUserExists = connection.prepareStatement("SELECT * FROM doctor WHERE email = ?");
                psCheckUserExists.setString(1, doctor.email);
                resultSet = psCheckUserExists.executeQuery();


                if (resultSet.isBeforeFirst()) {
                    System.out.println("User already exists!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You cannot use this Email!");
                    alert.show();
                } else {
                    psInsert = connection.prepareStatement("INSERT INTO doctor (firstname, lastname, address, phone, email, gender, password, dob, nic, specialty, licenseNumber  ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    psInsert.setString(1, doctor.firstname);
                    psInsert.setString(2, doctor.lastname);
                    psInsert.setString(3, doctor.address);
                    psInsert.setString(4, doctor.phone);
                    psInsert.setString(5, doctor.email);
                    psInsert.setString(6, String.valueOf(doctor.gender));
                    psInsert.setString(7, password);
                    psInsert.setDate(8, doctor.dateOfBirth);
                    psInsert.setString(9, doctor.nic);
                    psInsert.setString(10, doctor.specialty);
                    psInsert.setString(11, doctor.licenseNumber);

                    int r = psInsert.executeUpdate();
                    if(r > 0){
                        System.out.println("@DoctorClass registered as a Doctor");

                        System.out.println("Successfully Registered.");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Successfully Registered!");
                        alert.show();
                    } else {
                        System.out.println("error while registering");
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
