package com.example.laborator5;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Domain.Patient;
import com.example.laborator5.GUI.DentalGUIController;
import com.example.laborator5.Repository.IRepository;
import com.example.laborator5.Service.AppointmentService;
import com.example.laborator5.Service.PatientService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.laborator5.Main.Main.createAppointmentRepository;
import static com.example.laborator5.Main.Main.createPatientRepository;

public class DentalApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DentalApplication.class.getResource("Interface.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        IRepository<Integer, Appointment> appointmentRepository = createAppointmentRepository();
        IRepository<Integer, Patient> patientRepository = createPatientRepository();

        PatientService patientService = new PatientService(patientRepository);
        AppointmentService appointmentService = new AppointmentService(appointmentRepository);

        DentalGUIController dentalGUIController = fxmlLoader.getController();
        dentalGUIController.setServices(patientService, appointmentService);
        stage.setTitle("Dental");
        stage.setScene(scene);
        stage.show();
    }
}
