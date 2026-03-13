package com.dentist.appointment.repository;

import com.dentist.appointment.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DentistRepository extends JpaRepository<Dentist, Long> {

    List<Dentist> findByAvailable(boolean available);
}
