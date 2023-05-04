package sample.hms_project_team_12.Appointment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import sample.hms_project_team_12.MedicalRecord.MedicalRecord;
import sample.hms_project_team_12.User.Doctor;
import sample.hms_project_team_12.User.Patient;
import sample.hms_project_team_12.database.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class AppointmentSQL {

    // Create
    public static void createNewAppointment(Appointment appointment) {
        PreparedStatement psInsert = null;
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String createNewRecordQuery = "INSERT INTO appointment (doctor_id, patient_id, appointment_date, appointment_time, status ) VALUES (?, ?, ?, ?, ?)";

        try {
            psInsert = connectDB.prepareStatement(createNewRecordQuery);
            psInsert.setInt(1, appointment.getDoctor_id());
            psInsert.setInt(2, appointment.getPatient_id());
            psInsert.setDate(3, appointment.getAppointment_date());
            psInsert.setTime(4, appointment.getAppointment_time());
            psInsert.setString(5, appointment.getStatus().toString());
            int r = psInsert.executeUpdate();

            if(r > 0){
                System.out.println("Successfully created a Appointment");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Successfully Created a Appointment!");
                alert.show();
            } else {
                System.out.println("Error while Creating the Appointment");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error while Creating the Appointment!");
                alert.show();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Read
    public static Appointment getAppointmentById(int appointment_id) {
        MedicalRecord medicalRecord = null;
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String getRecordByIDAndPatientIDQuery = "SELECT * FROM hms.appointment WHERE appointment_id = ?";

        try {
            preparedStatement = connectDB.prepareStatement(getRecordByIDAndPatientIDQuery);
            preparedStatement.setInt(1, appointment_id);
            queryOutput = preparedStatement.executeQuery();

            if (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("appointment_id");
                int queryDoctorId = queryOutput.getInt("doctor_id");
                int queryPatientId = queryOutput.getInt("patient_id");
                java.sql.Date queryAppointmentDate = queryOutput.getDate("appointment_date");
                java.sql.Time queryAppointmentTime = queryOutput.getTime("appointment_time");
                Appointment.StatusTypes queryStatus = Appointment.StatusTypes.valueOf(queryOutput.getString("status").toUpperCase());
                return new Appointment(queryID, queryDoctorId, queryPatientId, queryAppointmentDate, queryAppointmentTime, queryStatus);
            } else {
                return null;
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
    }
    public static Appointment getAppointmentByIdAndPatientID(int appointment_id, int patient_id) {
        MedicalRecord medicalRecord = null;
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String getRecordByIDAndPatientIDQuery = "SELECT * FROM hms.appointment WHERE appointment_id = ? AND patient_id = ?";

        try {
            preparedStatement = connectDB.prepareStatement(getRecordByIDAndPatientIDQuery);
            preparedStatement.setInt(1, appointment_id);
            preparedStatement.setInt(2, patient_id);
            queryOutput = preparedStatement.executeQuery();

            if (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("appointment_id");
                int queryDoctorId = queryOutput.getInt("doctor_id");
                int queryPatientId = queryOutput.getInt("patient_id");
                java.sql.Date queryAppointmentDate = queryOutput.getDate("appointment_date");
                java.sql.Time queryAppointmentTime = queryOutput.getTime("appointment_time");
                Appointment.StatusTypes queryStatus = Appointment.StatusTypes.valueOf(queryOutput.getString("status"));
                return new Appointment(queryID, queryDoctorId, queryPatientId, queryAppointmentDate, queryAppointmentTime, queryStatus);
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

    public static Appointment getAppointmentByIdAndDoctorID(int appointment_id, int doctor_id) {
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String getRecordByIDAndPatientIDQuery = "SELECT * FROM hms.appointment WHERE appointment_id = ? AND patient_id = ?";

        try {
            preparedStatement = connectDB.prepareStatement(getRecordByIDAndPatientIDQuery);
            preparedStatement.setInt(1, appointment_id);
            preparedStatement.setInt(2, doctor_id);
            queryOutput = preparedStatement.executeQuery();

            if (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("appointment_id");
                int queryDoctorId = queryOutput.getInt("doctor_id");
                int queryPatientId = queryOutput.getInt("patient_id");
                java.sql.Date queryAppointmentDate = queryOutput.getDate("appointment_date");
                java.sql.Time queryAppointmentTime = queryOutput.getTime("appointment_time");
                Appointment.StatusTypes queryStatus = Appointment.StatusTypes.valueOf(queryOutput.getString("status"));

                Appointment appointment = new Appointment(queryID, queryDoctorId, queryPatientId, queryAppointmentDate, queryAppointmentTime, queryStatus);
                appointment.setDoctorName(Doctor.getNameById(queryDoctorId));
                appointment.setPatientName(Patient.getNameById(queryPatientId));

                return appointment;
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

    public static ObservableList<Appointment> getAppointmentByPatientID(int patient_id) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String getRecordByIDAndPatientIDQuery = "SELECT * FROM hms.appointment WHERE patient_id = ?";

        try {
            preparedStatement = connectDB.prepareStatement(getRecordByIDAndPatientIDQuery);
            preparedStatement.setInt(1, patient_id);
            queryOutput = preparedStatement.executeQuery();

            while (queryOutput.next()) {
                int queryID = queryOutput.getInt("appointment_id");
                int queryDoctorId = queryOutput.getInt("doctor_id");
                int queryPatientId = queryOutput.getInt("patient_id");
                java.sql.Date queryAppointmentDate = queryOutput.getDate("appointment_date");
                java.sql.Time queryAppointmentTime = queryOutput.getTime("appointment_time");
                Appointment.StatusTypes queryStatus = Appointment.StatusTypes.valueOf(queryOutput.getString("status").toUpperCase());

                Appointment appointment = new Appointment(queryID, queryDoctorId, queryPatientId, queryAppointmentDate, queryAppointmentTime, queryStatus);
                appointment.setDoctorName(Doctor.getNameById(queryDoctorId));
                appointments.add(appointment);
            }
            return appointments;

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
    }

    public static ObservableList<Appointment> getAllAppointmentList() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String getRecordByIDAndPatientIDQuery = "SELECT * FROM hms.appointment";

        try {
            preparedStatement = connectDB.prepareStatement(getRecordByIDAndPatientIDQuery);
            queryOutput = preparedStatement.executeQuery();

            while (queryOutput.next()) {
                int queryID = queryOutput.getInt("appointment_id");
                int queryDoctorId = queryOutput.getInt("doctor_id");
                int queryPatientId = queryOutput.getInt("patient_id");
                java.sql.Date queryAppointmentDate = queryOutput.getDate("appointment_date");
                java.sql.Time queryAppointmentTime = queryOutput.getTime("appointment_time");
                Appointment.StatusTypes queryStatus = Appointment.StatusTypes.valueOf(queryOutput.getString("status").toUpperCase());

                Appointment appointment = new Appointment(queryID, queryDoctorId, queryPatientId, queryAppointmentDate, queryAppointmentTime, queryStatus);
                appointment.setDoctorName(Doctor.getNameById(queryDoctorId));
                appointment.setPatientName(Patient.getNameById(queryPatientId));
                appointments.add(appointment);
            }
            return appointments;

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
    }

    public static boolean is_already_exist(int appointment_id) {
        PreparedStatement preparedStatement = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String checkDataQuery = "SELECT * FROM appointment WHERE appointment_id = ?";

        try {
            preparedStatement = connectDB.prepareStatement(checkDataQuery);
            preparedStatement.setInt(1, appointment_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true; // Doctor exists
            }
            else {
                return false; // Doctor does not exist
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }


    // Update
    public static void updateAppointment(Appointment appointment) {
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;
        String query = "UPDATE appointment SET status=? WHERE appointment_id=?";

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        try {
            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, appointment.getStatus().toString());
            preparedStatement.setInt(2, appointment.getAppointment_id());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 1) {
                System.out.println("Appointment data updated successfully.");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setContentText("Appointment data updated successfully.");
                alert.showAndWait();
            } else {
                System.out.println("Failed to update Appointment data.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Failed to update Appointment data.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete
    public static void deleteAppointmentById(int appointment_id) {
        PreparedStatement preparedStatement = null;
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String deleteRecordQuery = "DELETE FROM hms.appointment WHERE appointment_id = ?";

        try {
            preparedStatement = connectDB.prepareStatement(deleteRecordQuery);
            preparedStatement.setInt(1, appointment_id);
            int r = preparedStatement.executeUpdate();

            if (r > 0) {
                System.out.println("Successfully deleted appointment with ID " + appointment_id);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Successfully deleted appointment with ID " + appointment_id);
                alert.show();
            } else {
                System.out.println("No appointment found with ID " + appointment_id);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No appointment found with ID " + appointment_id);
                alert.show();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
