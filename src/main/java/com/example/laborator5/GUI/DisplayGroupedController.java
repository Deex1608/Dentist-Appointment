package com.example.laborator5.GUI;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Domain.Patient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DisplayGroupedController {
    @FXML
    private Accordion groupAccordion;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    public void displayGroupedPatients(Map<String, List<Patient>> groupedPatients) {
        groupAccordion.getPanes().clear();

        for (Map.Entry<String, List<Patient>> entry : groupedPatients.entrySet()) {
            String groupKey = entry.getKey();
            List<Patient> patients = entry.getValue();

            ListView<String> patientListView = new ListView<>(
                    patients.stream()
                            .map(p -> String.format("ID: %d | Name: %s | Email: %s", p.getId(), p.getName(), p.getEmail()))
                            .collect(Collectors.collectingAndThen(Collectors.toList(), FXCollections::observableArrayList))
            );

            TitledPane pane = new TitledPane();
            pane.setText(String.format("%s (%d Patients)", groupKey, patients.size()));

            pane.setContent(new VBox(patientListView));

            groupAccordion.getPanes().add(pane);
        }
    }

    public void displayGroupedAppointments(Map<String, List<Appointment>> groupedAppointments) {
        groupAccordion.getPanes().clear();

        for (Map.Entry<String, List<Appointment>> entry : groupedAppointments.entrySet()) {
            String groupKey = entry.getKey();
            List<Appointment> appointments = entry.getValue();

            ListView<String> appointmentListView = new ListView<>(
                    appointments.stream()
                            .map(a -> String.format("ID: %d | Date: %s | Time: %s", a.getId(), a.getAppointmentDate(), a.getAppointmentTime()))
                            .collect(Collectors.collectingAndThen(Collectors.toList(), FXCollections::observableArrayList))
            );

            TitledPane pane = new TitledPane();
            pane.setText(String.format("%s (%d Appointments)", groupKey, appointments.size()));
            pane.setContent(new VBox(appointmentListView));

            groupAccordion.getPanes().add(pane);
        }
    }
}
