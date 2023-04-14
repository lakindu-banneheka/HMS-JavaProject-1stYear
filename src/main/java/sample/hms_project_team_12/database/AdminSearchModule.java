package sample.hms_project_team_12.database;

public class AdminSearchModule {
    Integer admin_id;
    String email, password;

    // Constructor
    public AdminSearchModule(Integer admin_id, String email, String password) {
        this.admin_id = admin_id;
        this.email = email;
        this.password = password;
    }

    // Getter
    public Integer getAdmin_id() {
        return admin_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    // Setter
    public void setAdmin_id(Integer admin_id) {
        this.admin_id = admin_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
