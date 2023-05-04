package sample.hms_project_team_12.Appointment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import sample.hms_project_team_12.User.Doctor;
import sample.hms_project_team_12.database.DataBaseConnection;
import java.sql.*;

public class DoctorAvailability {
    private int id;
    private int doctor_id;
    private Date date;
    private Time time;
    private int number_of_appointments;
    private int max_number_of_appointments;
    private boolean is_available;


    public DoctorAvailability(int id, int doctor_id, Date date, Time time, int number_of_appointments, int max_number_of_appointments, boolean is_available) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.date = date;
        this.time = time;
        this.number_of_appointments = number_of_appointments;
        this.max_number_of_appointments = max_number_of_appointments;
        this.is_available = is_available;
    }
    public DoctorAvailability(int id, int doctor_id, Date date, Time time, boolean is_available) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.date = date;
        this.time = time;
        this.number_of_appointments = number_of_appointments;
        this.max_number_of_appointments = max_number_of_appointments;
        this.is_available = is_available;
    }

    public DoctorAvailability(int doctor_id, Date date, Time time, boolean is_available) {
        this.doctor_id = doctor_id;
        this.date = date;
        this.time = time;
        this.number_of_appointments = number_of_appointments;
        this.max_number_of_appointments = max_number_of_appointments;
        this.is_available = is_available;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public int getNumber_of_appointments() {
        return number_of_appointments;
    }

    public int getMax_number_of_appointments() {
        return max_number_of_appointments;
    }

    public boolean getIs_available() {
        return is_available;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setNumber_of_appointments(int number_of_appointments) {
        this.number_of_appointments = number_of_appointments;
    }

    public void setMax_number_of_appointments(int max_number_of_appointments) {
        this.max_number_of_appointments = max_number_of_appointments;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }


    // SQL

    // read data from DB
    public static ObservableList<DoctorAvailability> getDoctorAvailabilityListByDoctorId(int doc_id) {
        ObservableList<DoctorAvailability> doctorAvailabilityList = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;
        String getDataQuery = "SELECT * FROM doctor_availability WHERE doctor_id = ?";

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        try {
            preparedStatement = connectDB.prepareStatement(getDataQuery);
            preparedStatement.setInt(1,doc_id);
            queryOutput = preparedStatement.executeQuery();

            while (queryOutput.next()) {
                int queryID = queryOutput.getInt("id");
                int queryDoctorID = queryOutput.getInt("doctor_id");
                Date queryDate = queryOutput.getDate("date");
                Time queryTime = queryOutput.getTime("time");
                int queryNoOfAppointments = queryOutput.getInt("number_of_appointments");
                int queryMaxNoOfAppointments = queryOutput.getInt("max_number_of_appointments");
                boolean queryIsAvailable = queryOutput.getBoolean("is_available");

                DoctorAvailability newDoctorAvailability = new DoctorAvailability(queryID, queryDoctorID, queryDate, queryTime, queryNoOfAppointments, queryMaxNoOfAppointments, queryIsAvailable);
                doctorAvailabilityList.add(newDoctorAvailability);
            }
            return doctorAvailabilityList;
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
        return null;
    }

    //Update
    public static void updateDoctorAvailabilityListById(DoctorAvailability doctorAvailability){
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;
        String query = "UPDATE doctor_availability SET doctor_id=?, date=?, time=?, is_available=? WHERE id=?";

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        try {
            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setInt(1, doctorAvailability.getDoctor_id());
            preparedStatement.setDate(2, doctorAvailability.getDate());
            preparedStatement.setTime(3,doctorAvailability.getTime());
            preparedStatement.setBoolean(4, doctorAvailability.is_available);
            preparedStatement.setInt(5, doctorAvailability.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 1) {
                System.out.println("Doctor availability updated successfully.");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setContentText("Doctor availability updated successfully.");
                alert.showAndWait();
            } else {
                System.out.println("Failed to update doctor availability.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Failed to update doctor availability.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create new
    public static void createDoctorAvailability(DoctorAvailability doctorAvailability){
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;
        String query = "INSERT INTO doctor_availability (doctor_id, date, time, is_available) VALUES (?, ?, ?, ?)";

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        try {
            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setInt(1, doctorAvailability.getDoctor_id());
            preparedStatement.setDate(2, doctorAvailability.getDate());
            preparedStatement.setTime(3,doctorAvailability.getTime());
            preparedStatement.setBoolean(4, doctorAvailability.is_available);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 1) {
                System.out.println("Doctor availability created successfully.");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setContentText("Doctor availability created successfully.");
                alert.showAndWait();
            } else {
                System.out.println("Failed to create doctor availability.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Failed to create doctor availability.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
