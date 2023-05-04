package sample.hms_project_team_12.util;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.hms_project_team_12.Appointment.Appointment;
import sample.hms_project_team_12.Appointment.AppointmentSQL;
import sample.hms_project_team_12.Appointment.DoctorAvailability;
import sample.hms_project_team_12.MedicalRecord.MedicalRecord;
import com.jfoenix.controls.JFXTimePicker;

import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.atomic.AtomicReference;

public class CheckboxPopup extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public boolean[] showPopupAndGetCheckboxStates(String[] checkboxNames, boolean[] checkBoxStates, Button btn_filter) {

        btn_filter.setOnAction(event -> {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Filter By");



            CheckBox[] checkboxes = new CheckBox[checkboxNames.length];

            for (int i = 0; i < checkboxNames.length; i++) {
                checkboxes[i] = new CheckBox(checkboxNames[i]);
                checkboxes[i].setSelected(checkBoxStates[i]);
                VBox.setMargin(checkboxes[i], new Insets(5, 5, 0, 10));
            }


            VBox popupVBox = new VBox(checkboxes);
            popupVBox.setAlignment(Pos.TOP_LEFT);


            Button saveButton = new Button("Save");
            saveButton.setStyle("-fx-border-radius: 5px; -fx-font-size: 20px; -fx-font-weight: 600; -fx-font-family: Arial; -fx-background-color: #ededed; -fx-text-fill: #2B3467; -fx-alignment: left; -fx-padding: 0px 15px;");
            VBox.setMargin(saveButton, new Insets(15));

            VBox innerVBox = new VBox(checkboxes);
            innerVBox.setAlignment(Pos.CENTER_LEFT);
            innerVBox.setPadding(new Insets(10));


            saveButton.setOnAction(saveEvent -> {
                for (int i = 0; i < checkboxNames.length; i++) {
                    checkBoxStates[i] = checkboxes[i].isSelected();
                }
                popup.close();
            });

            saveButton.setStyle("-fx-background-color: #2B3467; -fx-text-fill: #fff; -fx-font-size: 14; -fx-cursor: hand;");
            saveButton.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(saveButton, Priority.ALWAYS);

            VBox outerVBox = new VBox(innerVBox, saveButton);
            outerVBox.setAlignment(Pos.CENTER);
            outerVBox.setSpacing(10);
            outerVBox.setPadding(new Insets(10, 10, 10, 10));

            Scene popupScene = new Scene(outerVBox, 400, 220);
            popup.setScene(popupScene);
            popup.showAndWait();
        });

        return checkBoxStates;
    }

    public void displayMedicalRecordDataInPopUp(MedicalRecord medicalRecord, Button btn_show) {

        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("View Record");

        Label recordIdLabel = new Label("Record ID:");
        TextField recordIdField = new TextField();

        Label doctorIdLabel = new Label("Doctor ID:");
        TextField doctorIdField = new TextField();

        Label patientIdLabel = new Label("Patient ID:");
        TextField patientIdField = new TextField();

        Label symptomsLabel = new Label("Symptoms:");
        TextField symptomsField = new TextField();

        Label diagnosesLabel = new Label("Diagnoses:");
        TextField diagnosesField = new TextField();

        Label treatmentsLabel = new Label("Treatments:");
        TextField treatmentsField = new TextField();

        Label notesLabel = new Label("Notes:");
        TextField notesField = new TextField();

        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker();

        Button closeButton = new Button("Close");

        // Set values
//        recordIdField.setText(medicalRecord.getRecord_id());


        VBox root = new VBox(10, doctorIdLabel, doctorIdField,
                symptomsLabel, symptomsField, diagnosesLabel, diagnosesField, treatmentsLabel, treatmentsField,
                notesLabel, notesField, dateLabel, datePicker, closeButton);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(closeButton, new Insets(0, 0, 0, 100));

        Scene scene = new Scene(root, 400, 500);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();

    }

    public AtomicReference<MedicalRecord> createMedicalRecordInPopUp(int patientId, int doctor_id) {

        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Create new Record");

        Label symptomsLabel = new Label("Symptoms:");
        TextField symptomsField = new TextField();

        Label diagnosesLabel = new Label("Diagnoses:");
        TextField diagnosesField = new TextField();

        Label treatmentsLabel = new Label("Treatments:");
        TextField treatmentsField = new TextField();

        Label notesLabel = new Label("Notes:");
        TextField notesField = new TextField();

        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker();

        Button submitButton = new Button("Submit");

        AtomicReference<MedicalRecord> medicalRecord = new AtomicReference<>();

        submitButton.setOnAction(e -> {
            int doctorId = doctor_id;
            String symptoms = symptomsField.getText();
            String diagnoses = diagnosesField.getText();
            String treatments = treatmentsField.getText();
            String notes = notesField.getText();

            medicalRecord.set(new MedicalRecord(doctorId, patientId, symptoms, diagnoses, treatments, notes, Date.valueOf(datePicker.getValue())));
            popupWindow.close();

        });

        VBox root = new VBox(10,
                symptomsLabel, symptomsField, diagnosesLabel, diagnosesField, treatmentsLabel, treatmentsField,
                notesLabel, notesField, dateLabel, datePicker, submitButton);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(submitButton, new Insets(0, 0, 0, 100));

        Scene scene = new Scene(root, 400, 500);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();

        return medicalRecord;
    }

    // Doctor Availability
    public static void editDoctorAvailabilityInPopUp(DoctorAvailability doctorAvailabilityData) {

        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Edit Doctor Availability");

        Label idLabel = new Label("ID : ");
        TextField idField = new TextField();

        Label dateLabel = new Label("Date : ");
        DatePicker datePicker = new DatePicker();

        Label timeLabel = new Label("Time (HH:mm:ss) : ");
        TextField timePicker = new TextField();

        Label isAvailableLabel = new Label("Is Available:");
        TextField isAvailableField = new TextField();

        Button editButton = new Button("Edit");

        // set Data
        idField.setText(Integer.toString(doctorAvailabilityData.getId()));
        idField.setEditable(false);

        datePicker.setValue(doctorAvailabilityData.getDate().toLocalDate());
        timePicker.setText(doctorAvailabilityData.getTime().toString());
        isAvailableField.setText(Boolean.toString(doctorAvailabilityData.getIs_available()));

        editButton.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            Date date = Date.valueOf(datePicker.getValue());
            Time time = null;
            try {
                time = Time.valueOf(timePicker.getText());
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Time");
                alert.setContentText("Please enter a time in the format of HH:mm:ss.");
                alert.showAndWait();
                ex.printStackTrace();
            }
            String isAvailable = isAvailableField.getText();

            if(isAvailable.toLowerCase().equals("true") || isAvailable.toLowerCase().equals("false")) {
                // update SQL
                DoctorAvailability.updateDoctorAvailabilityListById(new DoctorAvailability(id, doctorAvailabilityData.getDoctor_id(), date,time, Boolean.valueOf(isAvailable)));
                popupWindow.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("is Available must be 'true' or 'false'. ");
                alert.showAndWait();
            }
        });

        editButton.setStyle("-fx-background-color: #2B3467; -fx-text-fill: #fff; -fx-font-size: 14px; -fx-padding: 5px 10px; -fx-alignment: center; -fx-cursor: HAND");

        VBox root = new VBox(10, idLabel, idField,
                dateLabel, datePicker,
                timeLabel,
                timePicker,
                isAvailableLabel, isAvailableField,
                editButton);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_LEFT);
        VBox.setMargin(editButton, new Insets(0, 0, 0, 100));

        Scene scene = new Scene(root, 300, 350);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();
    }

    public static void addNewDoctorAvailabilityInPopUp(int doctor_id) {

        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("New Doctor Availability");
        Label title = new Label("Create Doctor Availability");


        Label dateLabel = new Label("Date : ");
        DatePicker datePicker = new DatePicker();

        Label timeLabel = new Label("Time (HH:mm:ss) : ");
        TextField timePicker = new TextField();

        Label isAvailableLabel = new Label("Is Available:");
        TextField isAvailableField = new TextField();

        Button createButton = new Button("Create");

        createButton.setOnAction(e -> {
            Date date = Date.valueOf(datePicker.getValue());
            Time time = null;
            try {
                time = Time.valueOf(timePicker.getText());
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Time");
                alert.setContentText("Please enter a time in the format of HH:mm:ss.");
                alert.showAndWait();
                ex.printStackTrace();
            }
            String isAvailable = isAvailableField.getText();

            if(isAvailable.toLowerCase().equals("true") || isAvailable.toLowerCase().equals("false")) {
                DoctorAvailability.createDoctorAvailability(new DoctorAvailability(doctor_id, date,time, Boolean.valueOf(isAvailable)));
                popupWindow.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("is Available must be 'true' or 'false'. ");
                alert.showAndWait();
            }
        });

        createButton.setStyle("-fx-background-color: #2B3467; -fx-text-fill: #fff; -fx-font-size: 14px; -fx-padding: 5px 10px; -fx-alignment: center; -fx-cursor: HAND");

        VBox root = new VBox(10, title,
                dateLabel, datePicker,
                timeLabel,
                timePicker,
                isAvailableLabel, isAvailableField,
                createButton);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_LEFT);
        VBox.setMargin(createButton, new Insets(0, 0, 0, 100));

        Scene scene = new Scene(root, 300, 350);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();
    }

    // Appointments
    public static void editAppointmentInPopUp(Appointment appointment) {

        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Edit Appointment");

        Label idLabel = new Label("ID : ");
        TextField idField = new TextField();

        Label doctorNameLabel = new Label("Doctor Name : ");
        TextField doctorNameField = new TextField();

        Label patientNameLabel = new Label("Patient Name : ");
        TextField patientNameField = new TextField();

        Label dateLabel = new Label("Date : ");
        DatePicker datePicker = new DatePicker();

        Label timeLabel = new Label("Time (HH:mm:ss) : ");
        TextField timePicker = new TextField();

        Label statusLabel = new Label("Status:");
        ComboBox<Appointment.StatusTypes> statusField = new ComboBox();
        statusField.getItems().addAll(new Appointment.StatusTypes[]{Appointment.StatusTypes.SCHEDULED, Appointment.StatusTypes.PAID, Appointment.StatusTypes.COMPLETED, Appointment.StatusTypes.CANCELLED});

        Button editButton = new Button("Update");

        // set Data
        idField.setText(Integer.toString(appointment.getAppointment_id()));
        idField.setEditable(false);

        doctorNameField.setText(appointment.getDoctorName());
        doctorNameField.setEditable(false);

        patientNameField.setText(appointment.getPatientName());
        patientNameField.setEditable(false);

        datePicker.setValue(appointment.getAppointment_date().toLocalDate());
        datePicker.setEditable(false);

        timePicker.setText(appointment.getAppointment_time().toString());
        timePicker.setEditable(false);

        statusField.setValue(appointment.getStatus());

        editButton.setOnAction(e -> {
            AppointmentSQL.updateAppointment(new Appointment(appointment.getAppointment_id(), appointment.getDoctor_id(), appointment.getPatient_id(), appointment.getAppointment_date(), appointment.getAppointment_time(), statusField.getValue()));
            popupWindow.close();
        });

        editButton.setStyle("-fx-background-color: #2B3467; -fx-text-fill: #fff; -fx-font-size: 14px; -fx-padding: 5px 10px; -fx-alignment: center; -fx-cursor: HAND");

        VBox root = new VBox(10,
                idLabel, idField,
                doctorNameLabel, doctorNameField,
                patientNameLabel, patientNameField,
                dateLabel, datePicker,
                timeLabel, timePicker,
                statusLabel, statusField,
                editButton);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_LEFT);
        VBox.setMargin(editButton, new Insets(0, 0, 0, 100));

        Scene scene = new Scene(root, 300, 500);

        popupWindow.setScene(scene);
        popupWindow.showAndWait();
    }


    @Override
    public void start(Stage stage) throws Exception {

    }
}
