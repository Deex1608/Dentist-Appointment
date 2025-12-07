package com.example.laborator5.UI;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Exceptions.IllegalVariableType;
import com.example.laborator5.Domain.Patient;
import com.example.laborator5.Service.AppointmentService;
import com.example.laborator5.Service.PatientService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;



public class UI {
    private PatientService patientService;
    private AppointmentService appointmentService;

    public UI(PatientService NewService, AppointmentService NewAppointmentService) {
        this.patientService = NewService;
        this.appointmentService = NewAppointmentService;
    }

    private void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("\t" + CONSTANTS.EXIT_PROGRAM + ". Exit\n");
        System.out.println("------------Patient Related\n");
        System.out.println("\t" + CONSTANTS.ADD_PATIENT + ". Add Patient");
        System.out.println("\t" + CONSTANTS.DISPLAY_ALL_PATIENTS + ". View Patients");
        System.out.println("\t" + CONSTANTS.REMOVE_PATIENT + ". Remove Patient");
        System.out.println("\t" + CONSTANTS.UPDATE_PATIENT + ". Update Patient");
        System.out.println("\t" + CONSTANTS.SEARCH_PATIENT + ". Search Patient\n");
        System.out.println("-------------Appointment Related\n");
        System.out.println("\t" + CONSTANTS.ADD_APPOINTMENT + ". Add Appointment");
        System.out.println("\t" + CONSTANTS.DISPLAY_ALL_APPOINTMENTS + ". View Appointments");
        System.out.println("\t" + CONSTANTS.REMOVE_APPOINTMENT + ". Remove Appointment");
        System.out.println("\t" + CONSTANTS.UPDATE_APPOINTMENT + ". Update Appointment");
        System.out.println("\t" + CONSTANTS.SEARCH_APPOINTMENT  + ". Search Appointment\n");
        System.out.println("-------------Filter\n");
        System.out.println("\t" + CONSTANTS.FILTER_PATIENT_BY_NAME + ". Filter Patients by Name");
        System.out.println("\t" + CONSTANTS.FILTER_PATIENT_BY_PROBLEM + ". Filter Patient by Problem");
        System.out.println("\t" + CONSTANTS.FILTER_PATIENT_BY_EMAIL_DOMAIN + ". Filter Patient by Email Domain");
        System.out.println("\t" + CONSTANTS.FILTER_PATIENT_BY_PROBLEM_AND_EMAIL_DOMAIN + ". Filter Patient by Problem and Email Domain");
        System.out.println("\t" + CONSTANTS.FILTER_APPOINTMENT_BY_DATE + ". Filter Appointment by Date");
        System.out.println("\t" + CONSTANTS.FILTER_APPOINTMENT_BY_TIME + ". Filter Appointment by Time");
        System.out.println("\t" + CONSTANTS.FILTER_APPOINTMENT_BY_PATIENT_ID + ". Filter Appointment by Patient ID");
        System.out.println("-------------Grouping\n");
        System.out.println("\t" + CONSTANTS.GROUP_PATIENTS_BY_PROBLEMS + ". Group Patients by Problems");
        System.out.println("\t" + CONSTANTS.GROUP_APPOINTMENT_BY_TIME + ". Group Appointments by Time");
        System.out.println("\t" + CONSTANTS.GROUP_APPOINTMENT_BY_DATE + ". Group Appointments by Date");
    }

    public void run(){
        while(true){
            try{
                this.printMenu();
                int option  = CONSTANTS.INITIALIZE_WITH_INVALID_OPTION;
                System.out.print("Enter your choice: ");
                Scanner input = new Scanner(System.in);
                option = input.nextInt();
                switch (option) {
                    case CONSTANTS.EXIT_PROGRAM:
                        return;

                    case CONSTANTS.ADD_PATIENT:
                        this.addPatient();
                        break;

                    case CONSTANTS.DISPLAY_ALL_PATIENTS:
                        this.getPatients();
                        break;

                    case CONSTANTS.REMOVE_PATIENT:
                        this.removePatient();
                        break;

                    case CONSTANTS.UPDATE_PATIENT:
                        this.updatePatient();
                        break;

                    case CONSTANTS.SEARCH_PATIENT:
                        this.searchPatient();
                        break;

                    case CONSTANTS.ADD_APPOINTMENT:
                        this.addAppointment();
                        break;

                    case CONSTANTS.DISPLAY_ALL_APPOINTMENTS:
                        this.getAppointments();
                        break;

                    case CONSTANTS.REMOVE_APPOINTMENT:
                        this.removeAppointment();
                        break;

                    case CONSTANTS.UPDATE_APPOINTMENT:
                        this.updateAppointment();
                        break;

                    case CONSTANTS.SEARCH_APPOINTMENT:
                        this.searchAppointment();
                        break;

                    case CONSTANTS.FILTER_PATIENT_BY_NAME:
                        this.filterPatientByName();
                        break;

                    case CONSTANTS.FILTER_PATIENT_BY_PROBLEM:
                        this.filterPatientByProblem();
                        break;

                    case CONSTANTS.FILTER_PATIENT_BY_EMAIL_DOMAIN:
                        this.filterByEmailDomain();
                        break;

                    case CONSTANTS.FILTER_PATIENT_BY_PROBLEM_AND_EMAIL_DOMAIN:
                        this.filterByAGivenProblemAndAnEmailDomain();
                        break;

                    case CONSTANTS.FILTER_APPOINTMENT_BY_DATE:
                        try {
                            this.filterAppointmentByDate();
                        } catch (IllegalVariableType e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case CONSTANTS.FILTER_APPOINTMENT_BY_TIME:
                        try {
                            this.filterAppointmentByTime();
                        } catch (IllegalVariableType e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case CONSTANTS.FILTER_APPOINTMENT_BY_PATIENT_ID:
                        this.filterByPatientId();
                        break;

                    case CONSTANTS.GROUP_PATIENTS_BY_PROBLEMS:
                        this.groupPatientsByProblem();
                        break;

                    case CONSTANTS.GROUP_APPOINTMENT_BY_DATE:
                        this.groupAppointmentsByDate();
                        break;

                    case CONSTANTS.GROUP_APPOINTMENT_BY_TIME:
                        this.groupAppointmentsByTime();
                        break;

                    default:
                        System.out.println("Invalid option");
                        break;
                }
            }catch (Exception e){
                System.out.println("INVALID DATA TYPE: " + e.getMessage());
            }
        }
    }

    private void addPatient() throws IllegalVariableType {
        System.out.print("Enter new Patient ID: ");
        Scanner input = new Scanner(System.in);
        Integer PatientId = input.nextInt();
        if (this.patientService.isInTheList(PatientId)){
            System.out.println("Patient with this ID is already in the list!");
            return;
        }
        System.out.print("Enter new Patient Name: ");
        input = new Scanner(System.in);
        String name = input.nextLine();
        System.out.print("Enter new Patient Telephone number: ");
        input = new Scanner(System.in);
        String telephone = input.nextLine();
        System.out.print("Enter new Patient email: ");
        input = new Scanner(System.in);
        String email = input.nextLine();
        System.out.print("Enter new Patient Problem: ");
        input = new Scanner(System.in);
        String problem = input.nextLine();
        this.patientService.addPatient(PatientId, name, telephone, email, problem);
        System.out.println("Patient Added Successfully!");


    }

    private void getPatients(){
        int INDEX = 1;
        for (Patient patient: this.patientService.getAllPatients()){
            System.out.println(INDEX + ". " + patient.toString());
            INDEX++;
        }
    }

    private void removePatient() throws IllegalVariableType{
        System.out.print("Enter Patient ID: ");
        Scanner input = new Scanner(System.in);
        int PatientId = input.nextInt();
        if (this.patientService.isInTheList(PatientId)){
            this.patientService.removePatient(PatientId);
            System.out.println("Patient has been removed.");
        } else {
            System.out.println("There no patient with the ID: " + PatientId);
        }

    }

    private void updatePatient() throws IllegalVariableType {
        System.out.print("Enter Patient ID: ");
        Scanner input = new Scanner(System.in);
        int OldPatientId = input.nextInt();
        if  (this.patientService.isInTheList(OldPatientId)){
            System.out.print("Enter new Patient Name: ");
            input = new Scanner(System.in);
            String name = input.nextLine();
            System.out.print("Enter new Patient Telephone number: ");
            input = new Scanner(System.in);
            String telephone = input.nextLine();
            System.out.print("Enter new Patient email: ");
            input = new Scanner(System.in);
            String email = input.nextLine();
            System.out.print("Enter new Patient Problem: ");
            input = new Scanner(System.in);
            String problem = input.nextLine();
            this.patientService.updatePatient(OldPatientId, name, telephone, email, problem);
            System.out.println("Patient has been updated.");
        } else {
            System.out.println("There no patient with the ID: " + OldPatientId);
        }
    }

    private void searchPatient() throws IllegalVariableType{
        System.out.print("Enter Patient ID: ");
        Scanner input = new Scanner(System.in);
        int PatientId = input.nextInt();
        if (this.patientService.isInTheList(PatientId)){
            Patient searchedPatient = this.patientService.findByIdAPatient(PatientId);
            System.out.println(searchedPatient.toString());
        } else {
            System.out.println("There no patient with the ID: " + PatientId);
        }

    }

    private void filterPatientByProblem(){
        System.out.print("Enter Patient Problem that you want to filter by: ");
        Scanner input = new Scanner(System.in);
        String problem = input.nextLine();
        Iterable<Patient> filteredPatient = this.patientService.filterByProblem(problem);
        if (filteredPatient != null){
            for (Patient patient : filteredPatient){
                System.out.println(patient.toString());
            }
        } else {
            System.out.println("There no patient with the problem: " + problem);
        }

    }

    private void filterPatientByName(){
        System.out.print("Enter Patient Name : ");
        Scanner input = new Scanner(System.in);
        String name = input.nextLine();
        Iterable<Patient> filteredPatient = this.patientService.filterByName(name);
        if (filteredPatient != null){
            System.out.println("The filtered Patients are: ");
            for (Patient patient : filteredPatient){
                System.out.println(patient.toString());
            }

        } else {
            System.out.println("There are no patient with the name: " + name);
        }

    }

    private void filterByAGivenProblemAndAnEmailDomain(){
        System.out.print("Enter Patient Problem that you want to filter by: ");
        Scanner input = new Scanner(System.in);
        String problem = input.nextLine();
        System.out.print("Enter Patient Email Domain that you want to filter by: ");
        input = new Scanner(System.in);
        String emailDomain = input.nextLine();
        List<String> filteredPatient = this.patientService.filterByAGivenProblemAndAnEmailDomain(problem, emailDomain);
        if (filteredPatient != null){
            for (String patientName : filteredPatient){
                System.out.println(patientName);
            }
        } else {
            System.out.println("There no patient with the problem: " + problem + " and with the email domain: " + emailDomain);
        }
    }

    private void filterByEmailDomain(){
        System.out.print("Enter Patient Email Domain that you want to filter by: ");
        Scanner input = new Scanner(System.in);
        String emailDomain = input.nextLine();
        List<String> filteredPatient = this.patientService.filterByEmailDomain(emailDomain);
        if (filteredPatient != null){
            for (String patientName : filteredPatient){
                System.out.println(patientName);
            }
        } else {
            System.out.println("There no patient with the email domain: " + emailDomain);
        }
    }

    private void groupPatientsByProblem() {
        Map<String, List<Patient>> groupedPatients = this.patientService.groupPatientsByProblem();
        if (groupedPatients != null){
            for (String problem : groupedPatients.keySet()){
                System.out.println("Problem: " + problem + " with patients");
                List<Patient> allPatients = groupedPatients.get(problem);
                for (Patient patient: allPatients){
                    System.out.println(patient.toString());
                }
            }
        } else {
            System.out.println("There no patient in the list");
        }
    }

    private void addAppointment() throws IllegalVariableType {
        System.out.print("Enter new Appointment ID: ");
        Scanner input = new Scanner(System.in);
        Integer AppointmentId = input.nextInt();
        if (this.appointmentService.isInTheList(AppointmentId)){
            System.out.println("Appointment with this ID is already in the list!");
            return;
        }
        System.out.print("Enter Patient ID: ");
        input = new Scanner(System.in);
        Integer PatientId = input.nextInt();
        System.out.println("Enter Appointment date: ");
        System.out.print("Enter the day: ");
        input = new Scanner(System.in);
        String day = input.nextLine();
        System.out.print("Enter the month: ");
        input = new Scanner(System.in);
        String month = input.nextLine();
        System.out.print("Enter the year: ");
        input = new Scanner(System.in);
        String year = input.nextLine();
        String date = String.format("%s/%s/%s", day, month, year);
        System.out.println("Enter Appointment time: ");
        System.out.print("Enter the hour: ");
        input = new Scanner(System.in);
        String hour = input.nextLine();
        System.out.print("Enter the minute: ");
        input = new Scanner(System.in);
        String minute = input.nextLine();
        String time = String.format("%s:%s", hour, minute);
        this.appointmentService.addAppointment(AppointmentId, PatientId, date, time);
        System.out.println("Appointment Added Successfully!");
    }

    private void getAppointments(){
        int INDEX = 1;
        for (Appointment appointment : this.appointmentService.getAllAppointments()){
            System.out.println(INDEX + ". " + appointment.toString());
            INDEX++;
        }
    }

    private void removeAppointment() throws IllegalVariableType{
        System.out.print("Enter Appointment ID: ");
        Scanner input = new Scanner(System.in);
        Integer  AppointmentID = input.nextInt();
        if (this.appointmentService.isInTheList(AppointmentID)){
            this.appointmentService.removeAppointment(AppointmentID);
            System.out.println("Appointment has been removed.");
        } else {
            System.out.println("There no patient with the ID: " + AppointmentID);
        }
    }

    private void updateAppointment() throws IllegalVariableType {
        System.out.print("Enter Appointment ID: ");
        Scanner input = new Scanner(System.in);
        Integer AppointmentID = input.nextInt();
        if (!this.appointmentService.isInTheList(AppointmentID)){
            System.out.println("There  no patient with the ID: " + AppointmentID);
            return;
        }
        System.out.print("Enter Patient ID: ");
        input = new Scanner(System.in);
        Integer PatientId = input.nextInt();
        System.out.println("Enter Appointment date: ");
        System.out.print("Enter the day: ");
        input = new Scanner(System.in);
        String day = input.nextLine();
        System.out.print("Enter the month: ");
        input = new Scanner(System.in);
        String month = input.nextLine();
        System.out.print("Enter the year: ");
        input = new Scanner(System.in);
        String year = input.nextLine();
        String date = String.format("%s/%s/%s", day, month, year);
        System.out.println("Enter Appointment time: ");
        System.out.print("Enter the hour: ");
        input = new Scanner(System.in);
        String hour = input.nextLine();
        System.out.print("Enter the minute: ");
        input = new Scanner(System.in);
        String minute = input.nextLine();
        String time = String.format("%s:%s", hour, minute);
        this.appointmentService.modifyAppointment(AppointmentID, PatientId, date, time);
        System.out.println("The Appointment has been updated.");
    }

    private void searchAppointment() throws IllegalVariableType{
        System.out.print("Enter Appointment ID: ");
        Scanner input = new Scanner(System.in);
        Integer AppointmentID = input.nextInt();
        if (!this.appointmentService.isInTheList(AppointmentID)){
            System.out.println("There no patient with the ID: " + AppointmentID);
            return;
        }
        Appointment searchedAppointment = this.appointmentService.findByIdAnAppointment(AppointmentID);
        System.out.println(searchedAppointment.toString());
    }

    private void filterAppointmentByDate() throws IllegalVariableType {
        System.out.println("Enter Appointment date to filter by: ");
        System.out.print("Enter the day: ");
        Scanner input = new Scanner(System.in);
        String day = input.nextLine();
        System.out.print("Enter the month: ");
        input = new Scanner(System.in);
        String month = input.nextLine();
        System.out.print("Enter the year: ");
        input = new Scanner(System.in);
        String year = input.nextLine();
        String date = String.format("%s/%s/%s", day, month, year);
        Iterable<Appointment> filteredAppointments = this.appointmentService.filterByAppointmentDate(date);
        if (filteredAppointments != null){
            for (Appointment appointment : filteredAppointments){
                System.out.println(appointment.toString());
            }
        } else {
            System.out.println("There are no patient with the date: " + date);
        }

    }

    private void filterAppointmentByTime() throws IllegalVariableType {
        System.out.println("Enter Appointment time to filter by: ");
        System.out.print("Enter the hour: ");
        Scanner input = new Scanner(System.in);
        String hour = input.nextLine();
        System.out.print("Enter the minute: ");
        input = new Scanner(System.in);
        String minute = input.nextLine();
        String time = String.format("%s:%s", hour, minute);
        Iterable<Appointment> filteredAppointment = this.appointmentService.filterByAppointmentTime(time);
        if (filteredAppointment != null){
            for (Appointment appointment : filteredAppointment){
                System.out.println(appointment.toString());
            }
        } else {
            System.out.println("There are no patient with the time: " + time);
        }

    }

    private void filterByPatientId(){
        System.out.print("Enter Patient ID you want to filter by: ");
        Scanner input = new Scanner(System.in);
        Integer PatientID = input.nextInt();
        List<Integer> filteredAppointments = this.appointmentService.filterByPatientId(PatientID);
        if (filteredAppointments != null){
            System.out.print("Appointments Ids for patient " + PatientID + ": ");
            for (Integer appoinmentId : filteredAppointments){
                System.out.print(appoinmentId + " ");
            }
        } else {
            System.out.println("There are no patient with the id: " + PatientID);
        }
    }

    private void groupAppointmentsByDate() {
        Map<String, List<Appointment>> groupedAppointments = this.appointmentService.groupAppointmentsByDate();
        if (groupedAppointments != null){
            for (String date : groupedAppointments.keySet()){
                System.out.println("Date: " + date + " with appointments");
                List<Appointment> allAppointments = groupedAppointments.get(date);
                for (Appointment appointment: allAppointments){
                    System.out.println(appointment.toString());
                }
            }
        } else {
            System.out.println("There no appointment in the list");
        }

    }

    private void groupAppointmentsByTime() {
        Map<String, List<Appointment>> groupedAppointments = this.appointmentService.groupAppointmentsByTime();
        if (groupedAppointments != null){
            for (String time : groupedAppointments.keySet()){
                System.out.println("Time: " + time + " with appointments");
                List<Appointment> allAppointments = groupedAppointments.get(time);
                for (Appointment appointment: allAppointments){
                    System.out.println(appointment.toString());
                }
            }
        } else {
            System.out.println("There no appointment in the list");
        }

    }
}
