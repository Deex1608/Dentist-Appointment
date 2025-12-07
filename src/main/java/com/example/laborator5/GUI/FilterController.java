package com.example.laborator5.GUI;

import com.example.laborator5.UI.CONSTANTS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.*;

public class FilterController implements Initializable {

    @FXML
    private ComboBox<String> filterCriterionComboBox;

    @FXML
    private TextField filterValueField;

    @FXML
    private Label valueLabel;

    @FXML
    private Label secondaryValueLabel;

    @FXML
    private TextField secondaryValueField;

    private final List<String> filterOptions = new ArrayList<>();

    private Stage dialogStage;
    private DentalGUIController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filterOptions.add("Filter Patients by Name");
        filterOptions.add("Filter Patient by Problem");
        filterOptions.add("Filter Patient by Email Domain");
        filterOptions.add("Filter Patient by Problem and Email Domain");
        filterOptions.add("Filter Appointment by Date");
        filterOptions.add("Filter Appointment by Time");
        filterOptions.add("Filter Appointment by Patient ID");

        ObservableList<String> items = FXCollections.observableArrayList(filterOptions);
        filterCriterionComboBox.setItems(items);

        filterCriterionComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleCriterionSelection() {
        String selected = filterCriterionComboBox.getSelectionModel().getSelectedItem();

        boolean requiresSecondaryInput = selected != null &&
                selected.equals("Filter Patient by Problem and Email Domain");

        secondaryValueLabel.setVisible(requiresSecondaryInput);
        secondaryValueField.setVisible(requiresSecondaryInput);
        if (requiresSecondaryInput) {
            valueLabel.setText("Enter Problem:");
            filterValueField.setPromptText("Dental problem (e.g., Cavities)");
        } else {
            if (selected != null && selected.contains("Date")) {
                valueLabel.setText("Enter Date:");
                filterValueField.setPromptText("YYYY-MM-DD");
            } else if (selected != null && selected.contains("Time")){
                valueLabel.setText("Enter Time:");
                filterValueField.setPromptText("HH:MM");
            }else {
                valueLabel.setText("Enter Value:");
                filterValueField.setPromptText("Enter required value...");
            }
        }
    }

    @FXML
    private void handleApplyFilter() {
        String displaySelection = filterCriterionComboBox.getSelectionModel().getSelectedItem();
        String filterValue = filterValueField.getText().trim();
        String secondaryFilterValue = secondaryValueField.getText().trim();

        if (displaySelection == null || filterValue.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a criterion and enter a value.").showAndWait();
            return;
        }

        if (displaySelection.equals("Filter Patient by Problem and Email Domain") && secondaryFilterValue.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter both the Problem and the Email Domain.").showAndWait();
            return;
        }

        int selectedIndex = filterCriterionComboBox.getSelectionModel().getSelectedIndex();
        String constantValue = filterOptions.get(selectedIndex);

        if (secondaryFilterValue.isEmpty()) {
            mainController.applyFilter(constantValue, filterValue);

        } else {
            String combinedValue = filterValue + "|" + secondaryFilterValue;
            mainController.applyFilter(constantValue, combinedValue);

        }


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