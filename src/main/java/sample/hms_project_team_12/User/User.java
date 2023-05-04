package sample.hms_project_team_12.User;

import java.io.IOException;
import java.sql.*;

abstract public class User implements IUser {

    public User() {

    }

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
    private int user_id;
    private String firstname;
    private String lastname;
    private String address;
    private String phone;
    private String email;
    private Gender gender;
    private Date dateOfBirth = Date.valueOf("2000-01-01");
    private String nic;
    private AccountType accountType;

    // User Constructors
    public User( String email) {
        this.email = email;
    }

    public User(int user_id, String firstname, String lastname, String address, String phone, String email, Gender gender, Date dateOfBirth, String nic) {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.nic = nic;
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

    public User(int user_id, String firstname, String lastname) {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
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

    // SQL - User
    abstract public User getUserDataById(int user_id);
    abstract public void updateUserDataById(User user, int userId);
    abstract public void createNewUserData();


}
