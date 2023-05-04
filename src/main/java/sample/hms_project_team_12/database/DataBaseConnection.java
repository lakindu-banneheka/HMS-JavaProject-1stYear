package sample.hms_project_team_12.database;

import javafx.scene.control.Alert;

import java.sql.*;

public class DataBaseConnection {
    public Connection databaseLink;
    public Connection getDBConnection() {

        String databaseName = "hms";
        String databaseUser = "root";
        String databasePassword = "123";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return databaseLink;
    }

}
