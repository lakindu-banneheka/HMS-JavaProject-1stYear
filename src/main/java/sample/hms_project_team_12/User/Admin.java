package sample.hms_project_team_12.User;

public class Admin {
    private int admin_id;
    private String email;
    private String password;

    public Admin(int admin_id, String email, String password) {
        this.admin_id = admin_id;
        this.email = email;
        this.password = password;
    }


    //getter
    public int getAdmin_id() {
        return admin_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    // setter
    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
