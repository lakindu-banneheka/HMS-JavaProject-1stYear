package sample.hms_project_team_12.MedicalRecord;

import javafx.scene.control.Alert;
import sample.hms_project_team_12.database.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class MedicalRecordSQL {

    public static MedicalRecord getRecordById(int record_id, int patient_id) {

        MedicalRecord medicalRecord = null;
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String getRecordByIDQuery = "SELECT * FROM hms.record WHERE record_id = ?";

        try {
            preparedStatement = connectDB.prepareStatement(getRecordByIDQuery);
            preparedStatement.setInt(1, patient_id);
            queryOutput = preparedStatement.executeQuery();

            if (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("record_id");
                int queryDoctorId = queryOutput.getInt("doctor_id");
                int queryPatientId = queryOutput.getInt("patient_id");
                String querySymptoms = queryOutput.getString("symptoms");
                String queryDiagnoses = queryOutput.getString("diagnoses");
                String queryTreatments = queryOutput.getString("treatments");
                String queryNotes = queryOutput.getString("notes");
                java.sql.Date querydate = queryOutput.getDate("date");

                medicalRecord = new MedicalRecord(queryID, queryDoctorId, queryPatientId, querySymptoms, queryDiagnoses, queryTreatments, queryNotes, querydate);
                return medicalRecord;
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

    public static void createNewMedicalRecord(MedicalRecord record) {
        PreparedStatement psInsert = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String createNewRecordQuery = "INSERT INTO record (doctor_id, patient_id, symptoms, diagnoses, treatments, notes, date ) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            psInsert = connectDB.prepareStatement(createNewRecordQuery);
            psInsert.setInt(1, record.getRecord_id());
            psInsert.setInt(2, record.getPatient_id());
            psInsert.setString(3, record.getSymptoms());
            psInsert.setString(4, record.getDiagnoses());
            psInsert.setString(5, record.getTreatments());
            psInsert.setString(6, record.getNotes());
            psInsert.setDate(7, java.sql.Date.valueOf(record.getDate().toString()));
            int r = psInsert.executeUpdate();

            if(r > 0){
                System.out.println("Successfully created a Record");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Successfully Created a Record!");
                alert.show();
            } else {
                System.out.println("Error while Creating the Record");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error while Creating the Record!");
                alert.show();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
