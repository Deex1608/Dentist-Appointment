package com.example.laborator5.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GroupingController implements Initializable {
    @FXML
    private ComboBox<String> groupCriterionComboBox;

    private final List<String> groupOptions = new ArrayList<>();

    private Stage dialogStage;
    private DentalGUIController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupOptions.add("Group Patients by Problems");
        groupOptions.add("Group Appointments by Time");
        groupOptions.add("Group Appointments by Date");

        ObservableList<String> items = FXCollections.observableArrayList(groupOptions);
        groupCriterionComboBox.setItems(items);

        groupCriterionComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleCriterionSelection() {
        String selected = groupCriterionComboBox.getSelectionModel().getSelectedItem();

    }

    @FXML
    private void handleApplyGroup() {
        String displaySelection = groupCriterionComboBox.getSelectionModel().getSelectedItem();

        if (displaySelection == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a criterion and enter a value.").showAndWait();
            return;
        }

        int selectedIndex = groupCriterionComboBox.getSelectionModel().getSelectedIndex();
        String constantValue = groupOptions.get(selectedIndex);

        mainController.applyGroup(constantValue);

        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainController(DentalGUIController mainController) {
        this.mainController = mainController;
    }
}
