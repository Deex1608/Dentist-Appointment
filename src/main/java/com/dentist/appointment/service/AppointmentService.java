package com.dentist.appointment.service;

import com.dentist.appointment.model.*;
import com.dentist.appointment.repository.AppointmentRepository;
import com.dentist.appointment.repository.DentistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DentistRepository dentistRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DentistRepository dentistRepository) {
        this.appointmentRepository = appointmentRepository;
        this.dentistRepository = dentistRepository;
    }

    @Transactional
    public Appointment bookAppointment(Patient patient, Long dentistId, LocalDate date,
                                       LocalTime time, String treatmentType, String notes) {
        Dentist dentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new IllegalArgumentException("Dentist not found"));

        boolean slotTaken = appointmentRepository
                .findByAppointmentDateAndDentist(date, dentist)
                .stream()
                .anyMatch(a -> a.getAppointmentTime().equals(time)
                        && a.getStatus() != AppointmentStatus.CANCELLED);

        if (slotTaken) {
            throw new IllegalStateException("This time slot is already booked for the selected dentist.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);
        appointment.setTreatmentType(treatmentType);
        appointment.setNotes(notes);
        appointment.setStatus(AppointmentStatus.PENDING);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsForPatient(Patient patient) {
        return appointmentRepository.findByPatientOrderByAppointmentDateDescAppointmentTimeDesc(patient);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAllByOrderByAppointmentDateDescAppointmentTimeDesc();
    }

    public List<Appointment> getAppointmentsForDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date);
    }

    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Transactional
    public Appointment updateStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void cancelAppointment(Long appointmentId, Patient requestingPatient) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (!appointment.getPatient().getId().equals(requestingPatient.getId())) {
            throw new SecurityException("Not authorized to cancel this appointment.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    public List<Dentist> getAvailableDentists() {
        return dentistRepository.findByAvailable(true);
    }

    public List<Dentist> getAllDentists() {
        return dentistRepository.findAll();
    }

    public long countByStatus(AppointmentStatus status) {
        return appointmentRepository.countByStatus(status);
    }

    public long countTodayAppointments() {
        return appointmentRepository.countByAppointmentDate(LocalDate.now());
    }

    public long countTotalPatients() {
        return appointmentRepository.findAll().stream()
                .map(a -> a.getPatient().getId())
                .distinct()
                .count();
    }
}
