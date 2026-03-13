package com.dentist.appointment.repository;

import com.dentist.appointment.model.Patient;
import com.dentist.appointment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUser(User user);

    Optional<Patient> findByUserId(Long userId);
}
