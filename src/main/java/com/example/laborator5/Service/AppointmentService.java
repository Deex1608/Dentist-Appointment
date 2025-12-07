package com.example.laborator5.Service;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Exceptions.IllegalVariableType;
import com.example.laborator5.Repository.IRepository;
import com.example.laborator5.Validation.AppointmentValidation;
import com.example.laborator5.Validation.ValidationVariables;
import com.example.laborator5.Filter.FilterAppointmentByDate;
import com.example.laborator5.Filter.FilterAppointmentByTime;
import com.example.laborator5.Repository.FilterRepositoryAppointment;
import com.example.laborator5.Repository.MemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AppointmentService implements IService<Integer> {
    private IRepository<Integer, Appointment> appointmentRepository;
    public AppointmentService(IRepository<Integer, Appointment> newAppointmentRepository) {
        this.appointmentRepository = newAppointmentRepository;
    }

    public Iterable<Appointment> getAllAppointments() {
        return appointmentRepository.getAll();
    }

    public void addAppointment(Integer AppointmentId, Integer PatientId, String AppointmentDate, String AppointmentTime) throws IllegalVariableType {
        AppointmentValidation AppointmentToBeVerified = new AppointmentValidation(AppointmentId, PatientId, AppointmentDate, AppointmentTime);
        Appointment newAppointment = new Appointment(AppointmentId, PatientId, AppointmentDate, AppointmentTime);
        appointmentRepository.add(AppointmentId, newAppointment);
    }

    public void removeAppointment(Integer AppointmentId) {
        appointmentRepository.delete(AppointmentId);
    }

    public void modifyAppointment(Integer AppointmentId, Integer PatientId, String AppointmentDate, String AppointmentTime) throws IllegalVariableType {
        AppointmentValidation AppointmentToBeVerified = new AppointmentValidation(AppointmentId, PatientId, AppointmentDate, AppointmentTime);
        Appointment newAppointment = new Appointment(AppointmentId, PatientId, AppointmentDate, AppointmentTime);
        appointmentRepository.modify(AppointmentId, newAppointment);
    }

    @Override
    public boolean isInTheList(Integer AppointmentId) throws IllegalVariableType {
        if(ValidationVariables.IsNotAGoodId(AppointmentId)) {
            throw new IllegalVariableType("This is not a valid ID! Please enter a natural number!");
        }
        return findByIdAnAppointment(AppointmentId) != null;
    }

    public Appointment findByIdAnAppointment(Integer AppointmentId) throws IllegalVariableType {
        if(ValidationVariables.IsNotAGoodId(AppointmentId)) {
            throw new IllegalVariableType("This is not a valid ID! Please enter a natural number!");
        }
        return appointmentRepository.findById(AppointmentId);
    }

    public Iterable<Appointment> filterByAppointmentDate(String AppointmentDate) {
        if (AppointmentDate == null || !ValidationVariables.isADate(AppointmentDate)) {
            throw new RuntimeException("Invalid Appointment Date");
        }
        FilterAppointmentByDate AppointmentFilter = new FilterAppointmentByDate(AppointmentDate);
        FilterRepositoryAppointment filterRepositoryAppointment = new FilterRepositoryAppointment(AppointmentFilter);
        for (Appointment appointment : appointmentRepository.getAll()) {
            filterRepositoryAppointment.add(appointment.getId(),  appointment);
        }
        return filterRepositoryAppointment.getAll();
    }

    public Iterable<Appointment> filterByAppointmentTime(String AppointmentTime)  {
        if (AppointmentTime == null || !ValidationVariables.isATime(AppointmentTime)) {
            throw new RuntimeException("Invalid Appointment Time");
        }
        FilterAppointmentByTime AppointmentFilter = new FilterAppointmentByTime(AppointmentTime);
        FilterRepositoryAppointment filterRepositoryAppointment = new FilterRepositoryAppointment(AppointmentFilter);
        for (Appointment appointment : appointmentRepository.getAll()) {
            filterRepositoryAppointment.add(appointment.getId(),  appointment);
        }
        return filterRepositoryAppointment.getAll();
    }

    // ----------------- Java 8 Stream Reports

    private List<Appointment> getAppointmentList(){
        Iterable<Appointment> appointmentIterable = this.getAllAppointments();
        return StreamSupport.stream(appointmentIterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    public Map<String, List<Appointment>> groupAppointmentsByTime(){
        List<Appointment> allAppoinments = this.getAppointmentList();
        return allAppoinments.stream()
                .collect(Collectors.groupingBy(Appointment::getAppointmentTime));
    }

    public Map<String, List<Appointment>> groupAppointmentsByDate(){
        List<Appointment> allAppoinments = this.getAppointmentList();
        return allAppoinments.stream()
                .collect(Collectors.groupingBy(Appointment::getAppointmentDate));
    }

    public List<Integer> filterByPatientId(Integer PatientId){
        List<Appointment> allAppoinments = this.getAppointmentList();
        return allAppoinments.stream()
                .filter(appointment -> appointment.getPatientId().equals(PatientId))
                .map(appointment -> appointment.getId())
                .collect(Collectors.toList());
    }
}
