package com.example.laborator5.GUI;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Domain.Patient;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Map;

public class DisplayController {
    @FXML
    private TableView<Patient> patientTableView;

    public void displayPatientResults(ObservableList<Patient> patientList) {
        patientTableView.setItems(patientList);
    }


}
