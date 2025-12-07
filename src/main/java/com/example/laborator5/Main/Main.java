package com.example.laborator5.Main;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Domain.Patient;
import com.example.laborator5.Repository.*;
import com.example.laborator5.Service.AppointmentService;
import com.example.laborator5.Service.PatientService;
import com.example.laborator5.UI.UI;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static IRepository<Integer, Patient> createPatientRepository(){
        IRepository<Integer, Patient> patientRepository = null;

        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/main/java/settings.properties"));

            String PatientRepositoryType = properties.getProperty("PatientRepositoryType");
            if (PatientRepositoryType.equals("memory")) {
                patientRepository = new MemoryRepository<>();
                Patient patient1 = new Patient(1001, "Alice Popescu", "+40 721 111 001", "alice.popescu@example.com", "Cavities");
                Patient patient2 = new Patient(1002, "Bogdan Ionescu", "+40 721 111 002", "bogdan.ionescu@example.com", "Gingivitis");
                Patient patient3 = new Patient(1003, "Carmen Radu", "+40 721 111 003", "carmen.radu@example.com", "Tooth Sensitivity");
                Patient patient4 = new Patient(1004, "Daniela Petrescu", "+40 721 111 004", "daniela.petrescu@example.com", "Malocclusion");
                Patient patient5 = new Patient(1005, "Eduard Marin", "+40 721 111 005", "eduard.marin@example.com", "Broken Teeth");
                patientRepository.add(patient1.getId(), patient1);
                patientRepository.add(patient2.getId(), patient2);
                patientRepository.add(patient3.getId(), patient3);
                patientRepository.add(patient4.getId(), patient4);
                patientRepository.add(patient5.getId(), patient5);
            }
            if (PatientRepositoryType.equals("csvfile")) {
                String repositoryPath = properties.getProperty("PatientRepositoryPath");
                patientRepository = new PatientTextFileRepository(repositoryPath);
            }
            if (PatientRepositoryType.equals("binaryfile")) {
                String repositoryPath = properties.getProperty("PatientRepositoryPath");
                File PatientFile = new File(repositoryPath);
                if (!PatientFile.exists()) {
                    PatientFile.createNewFile();
                }
                patientRepository = new PacientBinaryFileRepository(repositoryPath);
            }
            if (PatientRepositoryType.equals("databasefile")){
                String repositoryPath = properties.getProperty("PatientRepositoryPath");
                patientRepository = new PatientDatabaseRepository(repositoryPath);
            }
            if (PatientRepositoryType.equals("xmlfile")){
                String repositoryPath = properties.getProperty("PatientRepositoryPath");
                patientRepository = new PatientXMLRepository(repositoryPath);
            }

            if (PatientRepositoryType.equals("jsonfile")){
                String repositoryPath = properties.getProperty("PatientRepositoryPath");
                patientRepository = new PatientJSONRepository(repositoryPath);
            }

        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
        return patientRepository;
    }

    public static IRepository<Integer, Appointment> createAppointmentRepository(){
        IRepository<Integer, Appointment> appointmentRepository = null;
        Properties properties = new Properties();
        try{
            properties.load(new FileReader("src/main/java/settings.properties"));

            String AppointmentrepositoryType = properties.getProperty("AppointmentRepositoryType");
            if (AppointmentrepositoryType.equals("memory")) {
                appointmentRepository = new MemoryRepository<>();
                Appointment appointment1 = new Appointment(1, 1001, "05/11/2025", "09:00");
                Appointment appointment2 = new Appointment(2, 1002, "06/11/2025", "10:00");
                Appointment appointment3 = new Appointment(3, 1003, "31/10/2025", "14:00");
                Appointment appointment4 = new Appointment(4, 1004, "15/11/2025", "16:00");
                Appointment appointment5 = new Appointment(5, 1005, "31/10/2025", "16:00");

                appointmentRepository.add(appointment1.getId(), appointment1);
                appointmentRepository.add(appointment2.getId(), appointment2);
                appointmentRepository.add(appointment3.getId(), appointment3);
                appointmentRepository.add(appointment4.getId(), appointment4);
                appointmentRepository.add(appointment5.getId(), appointment5);
            }
            if (AppointmentrepositoryType.equals("csvfile")) {
                String repositoryPath = properties.getProperty("AppointmentRepositoryPath");
                appointmentRepository = new AppointmentTextFileRepository(repositoryPath);
            }
            if (AppointmentrepositoryType.equals("binaryfile")) {
                String repositoryPath = properties.getProperty("AppointmentRepositoryPath");
                File Appointmentfile = new File(repositoryPath);
                if (!Appointmentfile.exists()) {
                    Appointmentfile.createNewFile();
                }
                appointmentRepository = new AppointmentBinaryFileRepository(repositoryPath);
            }
            if (AppointmentrepositoryType.equals("databasefile")){
                String repositoryPath = properties.getProperty("AppointmentRepositoryPath");
                appointmentRepository = new AppointmentDatabaseRepository(repositoryPath);
            }

            if (AppointmentrepositoryType.equals("xmlfile")){
                String repositoryPath = properties.getProperty("AppointmentRepositoryPath");
                appointmentRepository = new AppointmentXMLRepository(repositoryPath);
            }

            if (AppointmentrepositoryType.equals("jsonfile")){
                String repositoryPath = properties.getProperty("AppointmentRepositoryPath");
                appointmentRepository = new AppointmentJSONRepository(repositoryPath);
            }
        }catch(IOException ioException){
            throw new RuntimeException(ioException);
        }
        return appointmentRepository;
    }

    public static void main(String[] user_input) {


        try{
            IRepository<Integer, Appointment> appointmentRepository = createAppointmentRepository();
            IRepository<Integer, Patient> patientRepository = createPatientRepository();

            PatientService patientService = new PatientService(patientRepository);
            AppointmentService appointmentService = new AppointmentService(appointmentRepository);

            UI ui = new UI(patientService, appointmentService);

            ui.run();

        }catch(Exception caughtException){
            System.out.println("Error: " + caughtException.getMessage());
        }



    }
}
