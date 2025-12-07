package com.example.laborator5.GUI;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Domain.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddModifyAppointmentController {

    private Stage dialogStage;
    private DentalGUIController mainController;
    private Object resultData = null;

    @FXML
    private Label newPatientEmailLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Label newPatientTelephoneLabel;

    @FXML
    private TextField newPatientIdTextField;

    @FXML
    private TextField newAppointmentTimeTextField;

    @FXML
    private TextField newAppointmentDateTextField;

    @FXML
    private TextField newAppointmentIdTextField;

    @FXML
    private Button addNewAppointmentButton;

    @FXML
    private Label newPatientIdLabel;

    @FXML
    private Label newPatientNameLabel;

    @FXML
    void addNewAppointmentButtonHandler(ActionEvent event) {
        try {
            int appointmentId = Integer.parseInt(newAppointmentIdTextField.getText());
            int patientId = Integer.parseInt(newPatientIdTextField.getText());
            String date = newAppointmentDateTextField.getText();
            String time = newAppointmentTimeTextField.getText();

            Appointment newAppointment = new Appointment(appointmentId, patientId, date, time);

            this.resultData = newAppointment;
            dialogStage.close();

        } catch (NumberFormatException numberFormatException) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ID must be a number.");
            alert.showAndWait();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void cancelButtonHandler(ActionEvent event) {
        this.resultData = null;
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainController(DentalGUIController mainController) {
        this.mainController = mainController;
    }

    public Object getResultData() {
        return resultData;
    }

}
