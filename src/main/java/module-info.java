module sample.hms_project_team_12 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;


    opens sample.hms_project_team_12 to javafx.fxml;
    exports sample.hms_project_team_12;
    exports sample.hms_project_team_12.User;
    opens sample.hms_project_team_12.User to javafx.fxml;
    exports sample.hms_project_team_12.util;
    opens sample.hms_project_team_12.util to javafx.fxml;
    opens sample.hms_project_team_12.database to javafx.base;
    opens sample.hms_project_team_12.MedicalRecord to javafx.base;
    opens sample.hms_project_team_12.Appointment to javafx.base;

}