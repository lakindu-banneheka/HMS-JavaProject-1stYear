package sample.hms_project_team_12.Appointment;

import java.sql.Time;
import java.sql.Date;

public class Appointment {
    public enum StatusTypes {
        SCHEDULED,
        PAID,
        CANCELLED,
        COMPLETED,
    }
    private int appointment_id;
    private int doctor_id;
    private String doctorName;
    private String patientName;
    private int patient_id;
    private Date appointment_date;
    private Time appointment_time;
    private StatusTypes status;


    // Constructor
    public Appointment(int appointment_id, int doctor_id, int patient_id, Date appointment_date, Time appointment_time, StatusTypes status) {
        this.appointment_id = appointment_id;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.status = status;
    }

    public Appointment(int doctor_id, int patient_id, Date appointment_date, Time appointment_time, StatusTypes status) {
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.status = status;
    }


    // Getter
    public int getAppointment_id() {
        return appointment_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public Date getAppointment_date() {
        return appointment_date;
    }

    public Time getAppointment_time() {
        return appointment_time;
    }

    public StatusTypes getStatus() {
        return status;
    }

    public String getPatientName() {
        return patientName;
    }

    // Setter
    public void setStatus(StatusTypes status) {
        this.status = status;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    public void setDoctorName(int doctorId) {

//        this.doctorName = doctorName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
