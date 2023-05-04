package sample.hms_project_team_12.User;

import java.sql.Date;

public interface IUser {
    // enums
    enum Gender {
        MALE,
        FEMALE
    }
    enum AccountType {
        PATIENT,
        DOCTOR,
        ADMIN
    }

    // attributes
    int user_id = 0;
    String firstname = null;
    String lastname = null;
    String address = null;
    String phone = null;
    String email = null;
    User.Gender gender = null;
    Date dateOfBirth = Date.valueOf("2000-01-01");
    String nic = null;
     User.AccountType accountType = null;

}
