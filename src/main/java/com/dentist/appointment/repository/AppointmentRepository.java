package com.dentist.appointment.repository;

import com.dentist.appointment.model.Appointment;
import com.dentist.appointment.model.AppointmentStatus;
import com.dentist.appointment.model.Dentist;
import com.dentist.appointment.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatient(Patient patient);

    List<Appointment> findByPatientOrderByAppointmentDateDescAppointmentTimeDesc(Patient patient);

    List<Appointment> findByDentist(Dentist dentist);

    List<Appointment> findByAppointmentDate(LocalDate date);

    List<Appointment> findByAppointmentDateAndDentist(LocalDate date, Dentist dentist);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findAllByOrderByAppointmentDateDescAppointmentTimeDesc();

    long countByStatus(AppointmentStatus status);

    long countByAppointmentDate(LocalDate date);
}
