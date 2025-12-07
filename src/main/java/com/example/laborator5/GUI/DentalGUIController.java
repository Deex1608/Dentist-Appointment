package com.example.laborator5.GUI;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Domain.Patient;
import com.example.laborator5.Exceptions.IllegalVariableType;
import com.example.laborator5.Service.AppointmentService;
import com.example.laborator5.Service.PatientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class DentalGUIController {
    PatientService patientService;
    AppointmentService appointmentService;

    private ObservableList<Patient> masterPatientData;
    private ObservableList<Appointment> masterAppointmentData;

    private Window mainStageWindow;

    public void setMainStageWindow(Window window){
        this.mainStageWindow = window;
    }

    public void setServices(PatientService newPatientService, AppointmentService newAppointmentService){
        this.patientService = newPatientService;
        this.appointmentService = newAppointmentService;

        this.masterPatientData = FXCollections.observableArrayList();
        this.masterAppointmentData = FXCollections.observableArrayList();

        loadMasterData();

        this.setColumn();
        this.populateTable();
    }

    private void loadMasterData() {
        Iterable<Patient> allPatients = this.patientService.getAllPatients();
        StreamSupport.stream(allPatients.spliterator(), false).forEach(masterPatientData::add);

        Iterable<Appointment> allAppointments = this.appointmentService.getAllAppointments();
        StreamSupport.stream(allAppointments.spliterator(), false).forEach(masterAppointmentData::add);
    }

    @FXML
    private Button deletePatientButton;

    @FXML
    private Button addPatientButton;

    @FXML
    private Button updatePatientButton;

    @FXML
    private TableColumn<Patient, Integer> patientIdColumn;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Patient, String> patientEmailColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentDateColumn;

    @FXML
    private TableColumn<Appointment, Integer> appointmentPatientIdColumn;

    @FXML
    private TableColumn<Appointment, String> patientNameColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentTimeColumn;

    @FXML
    private TableView<Patient> patientTableView;

    @FXML
    private StackPane mainContentPane;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;

    @FXML
    private TableColumn<Patient, String> patientPhoneNumberColumn;

    @FXML
    private TextField appointmentSearchField;

    @FXML
    private TableColumn<Patient, String> patientProblemColumn;

    @FXML
    private VBox appointmentView;

    @FXML
    private TextField patientSearchField;

    @FXML
    private VBox patientView;

    @FXML
    void handleAddPatient(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator5/AddDialogue.fxml"));
            AnchorPane root = loader.load();

            AddModifyPatientController addModifyPatientController = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Patient");

            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            addModifyPatientController.setDialogStage(dialogStage);
            addModifyPatientController.setMainController(this);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();

            Object result = addModifyPatientController.getResultData();
            if (result instanceof Patient) {
                Patient newPatient = (Patient) result;

                this.patientService.addPatient(newPatient.getId(), newPatient.getName(), newPatient.getTelephone(), newPatient.getEmail(), newPatient.getProblem());

                populateTablePatient();

            }

        } catch (IOException | IllegalVariableType ioException) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ioException.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void handleDeletePatient(ActionEvent event) {
        Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();

        if (selectedPatient != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Patient: " + selectedPatient.getId() + ". " + selectedPatient.getName());
            alert.setContentText("Are you sure you want to delete this patient and all related data?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    this.patientService.removePatient(selectedPatient.getId());

                    populateTablePatient();

                } catch (IllegalVariableType illegalVariableType) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, illegalVariableType.getMessage());
                    errorAlert.showAndWait();
                }
            }
        } else {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING, "Please select a patient to delete.");
            warningAlert.showAndWait();
        }
    }

    @FXML
    void handleUpdatePatient(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator5/ModifyDialogue.fxml"));
            AnchorPane root = loader.load();

            AddModifyPatientController addModifyPatientController = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update a Patient");

            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            addModifyPatientController.setDialogStage(dialogStage);
            addModifyPatientController.setMainController(this);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();

            Object result = addModifyPatientController.getResultData();
            if (result instanceof Patient) {
                Patient newPatient = (Patient) result;

                this.patientService.updatePatient(newPatient.getId(), newPatient.getName(), newPatient.getTelephone(), newPatient.getEmail(), newPatient.getProblem());

                populateTablePatient();

            }

        } catch (IOException | IllegalVariableType ioException) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ioException.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void handleSearchPatients(ActionEvent event) {
        String inputIdText = patientSearchField.getText().trim();

        if (inputIdText.isEmpty()) {
            patientTableView.setItems(this.masterPatientData);
            return;
        }

        try {
            Integer searchId = Integer.parseInt(inputIdText);

            Patient resultPatient = this.patientService.findByIdAPatient(searchId);

            int rowIndex = patientTableView.getItems().indexOf(resultPatient);

            if (rowIndex >= 0) {
                patientTableView.getSelectionModel().select(rowIndex);
                patientTableView.scrollTo(rowIndex);
                patientTableView.getFocusModel().focus(rowIndex);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Patient found in database but not currently visible in the table view.");
                alert.showAndWait();
            }

        } catch (NumberFormatException numberFormatException) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid Patient ID (number).");
            alert.showAndWait();
        } catch (IllegalVariableType illegalVariableType) {
            Alert alert = new Alert(Alert.AlertType.ERROR, illegalVariableType.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void handleShowPatients(ActionEvent event) {
        patientView.setVisible(true);
        appointmentView.setVisible(false);
        populateTablePatient();
    }

    @FXML
    void handleShowAppointments(ActionEvent event) {
        patientView.setVisible(false);
        appointmentView.setVisible(true);
        populateTableAppointment();
    }

    @FXML
    void handleAddAppointment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator5/AddAppointmentDialogue.fxml"));
            AnchorPane root = loader.load();

            AddModifyAppointmentController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Appointment");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            controller.setDialogStage(dialogStage);
            controller.setMainController(this);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            Object result = controller.getResultData();
            if (result instanceof Appointment) {
                Appointment newAppointment = (Appointment) result;

                this.appointmentService.addAppointment(newAppointment.getId(), newAppointment.getPatientId(), newAppointment.getAppointmentDate(), newAppointment.getAppointmentTime());
                populateTableAppointment();
            }

        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void handleDeleteAppointment(ActionEvent event) {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Appointment: " + selectedAppointment.getId() + ". " + selectedAppointment.getAppointmentDate() + " " + selectedAppointment.getAppointmentTime());
            alert.setContentText("Are you sure you want to delete the appointment on " + selectedAppointment.getAppointmentDate() + " at " + selectedAppointment.getAppointmentTime() + "?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                this.appointmentService.removeAppointment(selectedAppointment.getId());

                populateTableAppointment();

            }
        } else {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING, "Please select an appointment to delete.");
            warningAlert.showAndWait();
        }
    }

    @FXML
    void handleUpdateAppointment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator5/ModifyAppointmentDialogue.fxml"));
            AnchorPane root = loader.load();

            AddModifyAppointmentController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Appointment");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            controller.setDialogStage(dialogStage);
            controller.setMainController(this);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            Object result = controller.getResultData();
            if (result instanceof Appointment) {
                Appointment newAppointment = (Appointment) result;
                this.appointmentService.modifyAppointment(newAppointment.getId(), newAppointment.getPatientId(), newAppointment.getAppointmentDate(), newAppointment.getAppointmentTime());
                populateTableAppointment();
            }

        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void handleSearchAppointments(ActionEvent event) {
        String inputIdText = appointmentSearchField.getText().trim();

        if (inputIdText.isEmpty()) {
            appointmentTableView.setItems(this.masterAppointmentData);
            return;
        }

        try {
            Integer searchId = Integer.parseInt(inputIdText);

            Appointment resultAppointment = this.appointmentService.findByIdAnAppointment(searchId);

            int rowIndex = appointmentTableView.getItems().indexOf(resultAppointment);

            if (rowIndex >= 0) {
                appointmentTableView.getSelectionModel().select(rowIndex);
                appointmentTableView.scrollTo(rowIndex);
                appointmentTableView.getFocusModel().focus(rowIndex);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Appointment found in database but not currently visible in the table view.");
                alert.showAndWait();
            }

        } catch (NumberFormatException numberFormatException) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid Appointment ID (number).");
            alert.showAndWait();
        } catch (IllegalVariableType illegalVariableType) {
            Alert alert = new Alert(Alert.AlertType.ERROR, illegalVariableType.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize(){

    }

    private void setColumn(){
        setColumnPatient();
        setColumnAppointment();
    }

    private void setColumnPatient(){
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        patientPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("Telephone"));
        patientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        patientProblemColumn.setCellValueFactory(new PropertyValueFactory<>("Problem"));
    }

    private void setColumnAppointment(){
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        appointmentPatientIdColumn.setCellValueFactory(new PropertyValueFactory<>("PatientId"));
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentDate"));
        appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentTime"));
    }

    public void populateTable(){
        populateTablePatient();
        populateTableAppointment();
    }

    private void populateTablePatient(){
        if(this.patientService == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Patient Service is null");
            alert.showAndWait();
        }
        Iterable<Patient> patientIterable = this.patientService.getAllPatients();
        ArrayList<Patient> allPatients = new ArrayList<>();
        for (Patient patient: patientIterable){
            allPatients.add(patient);
        }
        patientTableView.getItems().setAll(allPatients);
    }

    private void populateTableAppointment(){
        if(this.appointmentService == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Appointment Service is null");
            alert.showAndWait();
        }
        Iterable<Appointment> appointmentIterable = this.appointmentService.getAllAppointments();
        ArrayList<Appointment> allAppointments = new ArrayList<>();
        for(Appointment appointment: appointmentIterable){
            allAppointments.add(appointment);
        }

        appointmentTableView.getItems().setAll(allAppointments);
    }

    @FXML
    void handleFilterBy(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator5/FilterDialogue.fxml"));
            AnchorPane root = loader.load();
            FilterController filterController = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Filter Options");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            filterController.setDialogStage(dialogStage);
            filterController.setMainController(this);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Filter Dialog: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void applyFilter(String filterType, String filterValue) {
        final String FILTER_PATIENT_BY_NAME = "Filter Patients by Name";
        final String FILTER_PATIENT_BY_PROBLEM = "Filter Patient by Problem";
        final String FILTER_PATIENT_BY_EMAIL_DOMAIN = "Filter Patient by Email Domain";
        final String FILTER_PATIENT_BY_PROBLEM_AND_EMAIL_DOMAIN = "Filter Patient by Problem and Email Domain";

        final String FILTER_APPOINTMENT_BY_DATE = "Filter Appointment by Date";
        final String FILTER_APPOINTMENT_BY_TIME = "Filter Appointment by Time";
        final String FILTER_APPOINTMENT_BY_PATIENT_ID = "Filter Appointment by Patient ID";

        List<String> patientNamesResult = null;
        List<Integer> appointmentIdsResult = null;
        Iterable<Patient> patientIterable = null;
        Iterable<Appointment> appointmentIterable = null;


        boolean isPatientFilter = false;

        try {
            switch (filterType) {

                case FILTER_PATIENT_BY_NAME:
                    isPatientFilter = true;
                    patientIterable = this.patientService.filterByName(filterValue);
                    break;

                case FILTER_PATIENT_BY_PROBLEM:
                    isPatientFilter = true;
                    patientIterable = this.patientService.filterByProblem(filterValue);
                    break;

                case FILTER_PATIENT_BY_EMAIL_DOMAIN:
                    isPatientFilter = true;
                    patientNamesResult = this.patientService.filterByEmailDomain(filterValue);
                    break;

                case FILTER_PATIENT_BY_PROBLEM_AND_EMAIL_DOMAIN:
                    isPatientFilter = true;
                    String[] parts = filterValue.split("\\|", 2);
                    if (parts.length == 2) {
                        String problem = parts[0].trim();
                        String emailDomain = parts[1].trim();
                        patientNamesResult = this.patientService.filterByAGivenProblemAndAnEmailDomain(problem, emailDomain);
                    }
                    break;

                case FILTER_APPOINTMENT_BY_DATE:
                    appointmentIterable = this.appointmentService.filterByAppointmentDate(filterValue);
                    break;

                case FILTER_APPOINTMENT_BY_TIME:
                    appointmentIterable = this.appointmentService.filterByAppointmentTime(filterValue);
                    break;

                case FILTER_APPOINTMENT_BY_PATIENT_ID:
                    appointmentIdsResult = this.appointmentService.filterByPatientId(Integer.parseInt(filterValue));
                    break;

                default:
                    throw new RuntimeException("Incorrect Case!");
            }

        } catch (NumberFormatException numberFormatException) {
            Alert alert = new Alert(Alert.AlertType.ERROR, numberFormatException.getMessage());
            alert.showAndWait();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.showAndWait();
        }

        if (isPatientFilter) {
            ObservableList<Patient> obsPatientList = FXCollections.observableArrayList();

            if (patientIterable != null) {
                StreamSupport.stream(patientIterable.spliterator(), false).forEach(obsPatientList::add);

            } else if (patientNamesResult != null) {
                Iterable<Patient> allPatients = this.patientService.getAllPatients();

                List<String> finalPatientNamesResult = patientNamesResult;
                StreamSupport.stream(allPatients.spliterator(), false)
                        .filter(p ->
                            finalPatientNamesResult.contains(p.getName())
                        )
                        .forEach(obsPatientList::add);
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator5/DisplayDialogue.fxml"));
                AnchorPane root = loader.load();
                DisplayController displayController = loader.getController();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Filtered Results");
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                if(this.mainStageWindow != null){
                    dialogStage.initOwner(this.mainStageWindow);
                }

                displayController.displayPatientResults(obsPatientList);
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.showAndWait();

            } catch (IOException ioException) {
                System.err.println("Error loading Display Dialogue: " + ioException.getMessage());
            }

        } else {
            ObservableList<Appointment> obsAppointmentList = FXCollections.observableArrayList();

            if (appointmentIterable != null) {
                StreamSupport.stream(appointmentIterable.spliterator(), false).forEach(obsAppointmentList::add);

            }else if (appointmentIdsResult != null) {
                Iterable<Appointment> allAppointments = this.appointmentService.getAllAppointments();

                List<Integer> finalAppointmentIdsResult = appointmentIdsResult;
                StreamSupport.stream(allAppointments.spliterator(), false)
                        .filter(appointment -> finalAppointmentIdsResult.contains(appointment.getId()))
                        .forEach(obsAppointmentList::add);
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator5/DisplayAppointmentDialogue.fxml"));
                AnchorPane root = loader.load();
                DisplayAppointmentController displayController = loader.getController();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Filtered Results");
                dialogStage.initModality(Modality.APPLICATION_MODAL);

                if (this.mainStageWindow != null) {
                    dialogStage.initOwner(this.mainStageWindow);
                }

                displayController.displayAppointmentResults(obsAppointmentList);
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.showAndWait();

            } catch (IOException e) {
                System.err.println("Error loading Display Dialogue: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleGroupBy(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator5/GroupDialogue.fxml"));
            AnchorPane root = loader.load();
            GroupingController groupingController = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Group Options");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            groupingController.setDialogStage(dialogStage);
            groupingController.setMainController(this);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Group Dialog: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void applyGroup(String groupType) {
        final String GROUP_PATIENTS_BY_PROBLEM = "Group Patients by Problems";
        final String GROUP_APPOINTMENTS_BY_TIME = "Group Appointments by Time";
        final String GROUP_APPOINTMENTS_BY_DATE = "Group Appointments by Date";

        Map<String, List<Patient>> patientNamesResult = null;
        Map<String, List<Appointment>> appointmentIdsResult = null;


        boolean isPatientGroup = false;

        try {
            switch (groupType) {

                case GROUP_PATIENTS_BY_PROBLEM:
                    isPatientGroup = true;
                    patientNamesResult = this.patientService.groupPatientsByProblem();
                    break;

                case GROUP_APPOINTMENTS_BY_DATE:
                    appointmentIdsResult = this.appointmentService.groupAppointmentsByDate();
                    break;

                case GROUP_APPOINTMENTS_BY_TIME:
                    appointmentIdsResult = this.appointmentService.groupAppointmentsByTime();
                    break;

                default:
                    throw new RuntimeException("Incorrect Case!");
            }

        } catch (NumberFormatException numberFormatException) {
            Alert alert = new Alert(Alert.AlertType.ERROR, numberFormatException.getMessage());
            alert.showAndWait();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.showAndWait();
        }

        if (isPatientGroup) {
            ObservableList<Patient> obsPatientList = FXCollections.observableArrayList();

            if (patientNamesResult != null) {
                patientNamesResult.values().stream()
                        .flatMap(List::stream)
                        .forEach(obsPatientList::add);
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator5/DisplayGroupedDialogue.fxml"));
                AnchorPane root = loader.load();
                DisplayGroupedController displayGroupedController = loader.getController();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Grouped Results");
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                if(this.mainStageWindow != null){
                    dialogStage.initOwner(this.mainStageWindow);
                }

                displayGroupedController.displayGroupedPatients(patientNamesResult);
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.showAndWait();

            } catch (IOException ioException) {
                System.err.println("Error loading Display Dialogue: " + ioException.getMessage());
            }

        } else {
            ObservableList<Appointment> obsAppointmentList = FXCollections.observableArrayList();

           if (appointmentIdsResult != null) {
               appointmentIdsResult.values().stream()
                       .flatMap(List::stream)
                       .forEach(obsAppointmentList::add);

            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/laborator5/DisplayGroupedDialogue.fxml"));
                AnchorPane root = loader.load();
                DisplayGroupedController displayGroupedController = loader.getController();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Grouped Results");
                dialogStage.initModality(Modality.APPLICATION_MODAL);

                if (this.mainStageWindow != null) {
                    dialogStage.initOwner(this.mainStageWindow);
                }

                displayGroupedController.displayGroupedAppointments(appointmentIdsResult);
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.showAndWait();

            } catch (IOException e) {
                System.err.println("Error loading Display Dialogue: " + e.getMessage());
            }
        }
    }

}
