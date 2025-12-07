package com.example.laborator5.GUI;

import com.example.laborator5.Domain.Appointment;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Map;

public class DisplayAppointmentController {
    @FXML
    private TableView<Appointment> appointmentTableView;

    public void displayAppointmentResults(ObservableList<Appointment> appointmentList){
        appointmentTableView.setItems(appointmentList);
    }


}
