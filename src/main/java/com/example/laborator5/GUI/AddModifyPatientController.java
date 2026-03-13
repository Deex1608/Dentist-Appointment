package com.example.laborator5.GUI;

import com.example.laborator5.Domain.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class AddModifyPatientController {
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
    private Label newPatientProblemLabel;

    @FXML
    private TextField newPatientIdTextField;

    @FXML
    private Button addNewPatientButton;

    @FXML
    private TextField newPatientEmailTextField;

    @FXML
    private TextField newPatientProblemTextField;

    @FXML
    private TextField newPatientTelephoneTextField;

    @FXML
    private Label newPatientIdLabel;

    @FXML
    private Label newPatientNameLabel;

    @FXML
    private TextField newPatientNameTextField;

    @FXML
    void addNewPatientButtonHandler(ActionEvent event) {
        try {
            Integer patientId = Integer.parseInt(newPatientIdTextField.getText());
            String name = newPatientNameTextField.getText();
            String phoneNumber = newPatientTelephoneTextField.getText();
            String email = newPatientEmailTextField.getText();
            String problem = newPatientProblemTextField.getText();
            Patient newPatient = new Patient(patientId, name, phoneNumber, email, problem);

            this.resultData = newPatient;
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
