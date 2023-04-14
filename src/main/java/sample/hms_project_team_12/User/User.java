package sample.hms_project_team_12.User;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import sample.hms_project_team_12.SceneController;

import java.io.IOException;
import java.sql.*;

public class User {
    // enums
    public enum Gender {
        MALE,
        FEMALE
    }
    public enum AccountType {
        PATIENT,
        DOCTOR,
        ADMIN
    }

    // attributes
    protected int user_id;
    protected String firstname;
    protected String lastname;
    protected String address;
    protected String phone;
    protected String email;
    protected Gender gender;
    protected Date dateOfBirth = Date.valueOf("2000-01-01");
    protected String nic;
    protected AccountType accountType;

    // User Constructors
    public User( String email) {
        this.email = email;
    }
    public User(String firstname, String lastname, String address, String phone, String email, String gender, Date dateOfBirth, String nic, AccountType accountType) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.gender = (Gender.MALE.toString().compareToIgnoreCase(gender) <= 0 ? Gender.MALE : (Gender.FEMALE.toString().compareToIgnoreCase(gender) <= 0 ? Gender.FEMALE : null));
        this.dateOfBirth = dateOfBirth;
        this.accountType = accountType;
        this.nic = nic;
    }
    public User(int user_id,String firstname, String lastname, String address, String phone, String email, String gender, Date dateOfBirth, String nic, AccountType accountType) {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.gender = (Gender.MALE.toString().compareToIgnoreCase(gender) <= 0 ? Gender.MALE : (Gender.FEMALE.toString().compareToIgnoreCase(gender) <= 0 ? Gender.FEMALE : null));
        this.dateOfBirth = dateOfBirth;
        this.accountType = accountType;
        this.nic = nic;
    }

    public User(int user_id,String firstname, String lastname, String phone, String email, String gender) {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.gender = (Gender.MALE.toString().compareToIgnoreCase(gender) <= 0 ? Gender.MALE : (Gender.FEMALE.toString().compareToIgnoreCase(gender) <= 0 ? Gender.FEMALE : null));
    }
    // Getter
    public String getEmail() {
        return email;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public Gender getGender() {
        return gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNic() {
        return nic;
    }

    // Setter


    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }


    // -------------------------------------------------------------------------------------------------------------------
    // User login (patient, doctor, admin) - connection with database
    public static void userLogIn(ActionEvent event, String email, String password, AccountType accountType) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            if(email.contains("@")){
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "123");
                preparedStatement = connection.prepareStatement("SELECT password, accountType FROM users WHERE email = ?");
                preparedStatement.setString(1,email);
                resultSet = preparedStatement.executeQuery();

                if(!resultSet.isBeforeFirst()) {
                    System.out.println("email not found in the database!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Provided credentials are incorrect!");
                    alert.show();
                } else {
                    while (resultSet.next()) {
                        String retrievePassword = resultSet.getString("password");
                        String retrieveAccountType = resultSet.getString("accountType");

                        if(retrievePassword.equals(password)){
                            if(retrieveAccountType.equalsIgnoreCase(accountType.toString())){
                                System.out.println("@User87 login");
                                try {
//                                    switch (accountType) {
//                                        case ADMIN :
//                                            SceneController.switchScene(event,"admin_doctor-register-view.fxml","ABC Hospital - Admin");
//                                            break;
//                                        case DOCTOR:
//                                            SceneController.switchScene(event,"doctor-view.fxml","ABC Hospital - Admin");
//                                            break;
//                                        case PATIENT:
//                                            SceneController.switchScene(event,"patient-view.fxml","ABC Hospital - Admin");
//                                            break;
//                                    }

                                SceneController.switchScene(event,"admin_doctor-register-view.fxml","ABC Hospital - Admin");

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            } else {
                                System.out.println("account type did not match");
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Provided credentials are incorrect!");
                                alert.show();
                            }
                        } else {
                            System.out.println("password did not match");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Provided credentials are incorrect!");
                            alert.show();
                        }
                    }
                }
            } else {
                System.out.println("Invalid email");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided email is invalid!");
                alert.show();
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

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}
