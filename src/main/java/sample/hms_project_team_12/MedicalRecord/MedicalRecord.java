package sample.hms_project_team_12.MedicalRecord;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import sample.hms_project_team_12.database.DataBaseConnection;
import sample.hms_project_team_12.util.ErrorChecking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class MedicalRecord {
    private int record_id;
    private int doctor_id;
    private String doctor_name;
    private int patient_id;
    private String symptoms;
    private String diagnoses;
    private String treatments;
    private String notes;
    private Date date;

    // Constructors
    public MedicalRecord(int record_id, int doctor_id, int patient_id, String symptoms, String diagnoses, String treatments, String notes, Date date) {
        this.record_id = record_id;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.symptoms = symptoms;
        this.diagnoses = diagnoses;
        this.treatments = treatments;
        this.notes = notes;
        this.date = date;
    }
    public MedicalRecord(int doctor_id, int patient_id, String symptoms, String diagnoses, String treatments, String notes, Date date) {
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.symptoms = symptoms;
        this.diagnoses = diagnoses;
        this.treatments = treatments;
        this.notes = notes;
        this.date = date;
    }
    public MedicalRecord() {

    }

    //Getter
    public int getRecord_id() {
        return record_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getDiagnoses() {
        return diagnoses;
    }

    public String getTreatments() {
        return treatments;
    }

    public String getNotes() {
        return notes;
    }

    public Date getDate() {
        return date;
    }

    // Setter
    public void setDoctorName(String doctorName) {
        this.doctor_name = doctorName;
    }


    // Record SQL

    // get Medical Data Of a Patient By id
    public ObservableList<MedicalRecord> getMedicalDataOfPatientById(int patient_id) {
        ObservableList<MedicalRecord> recordSearchModuleObservableList = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet queryOutput = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String getRecordByIDQuery = "SELECT * FROM hms.record WHERE patient_id = ?";

        try {
            preparedStatement = connectDB.prepareStatement(getRecordByIDQuery);
            preparedStatement.setInt(1, patient_id);
            queryOutput = preparedStatement.executeQuery();

            while (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("record_id");
                int queryDoctorId = queryOutput.getInt("doctor_id");
                int queryPatientId = queryOutput.getInt("patient_id");
                String querySymptoms = queryOutput.getString("symptoms");
                String queryDiagnoses = queryOutput.getString("diagnoses");
                String queryTreatments = queryOutput.getString("treatments");
                String queryNotes = queryOutput.getString("notes");
                java.sql.Date querydate = queryOutput.getDate("date");

                // Populate the ObservableList
                recordSearchModuleObservableList.add(new MedicalRecord(queryID, queryDoctorId, queryPatientId, querySymptoms, queryDiagnoses, queryTreatments, queryNotes, querydate));
            }

            return recordSearchModuleObservableList;
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
        }}

    public MedicalRecord getRecordDataById(int record_id) {

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
        return medicalRecord;
    }

    public void createNewMedicalRecord(MedicalRecord record) {
        PreparedStatement psInsert = null;

        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        // SQL Query - Executed in the backend database
        String createNewRecordQuery = "INSERT INTO record (doctor_id, patient_id, symptoms, diagnoses, treatments, notes, date ) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            psInsert = connectDB.prepareStatement(createNewRecordQuery);
            psInsert.setInt(1, record.doctor_id);
            psInsert.setInt(2, record.patient_id);
            psInsert.setString(3, record.symptoms);
            psInsert.setString(4, record.diagnoses);
            psInsert.setString(5, record.treatments);
            psInsert.setString(6, record.notes);
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
