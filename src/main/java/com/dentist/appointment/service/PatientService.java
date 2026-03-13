package com.dentist.appointment.service;

import com.dentist.appointment.model.Patient;
import com.dentist.appointment.model.Role;
import com.dentist.appointment.model.User;
import com.dentist.appointment.repository.PatientRepository;
import com.dentist.appointment.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Patient registerPatient(String username, String rawPassword, String email,
                                   String firstName, String lastName, String phone) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken: " + username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        User user = new User(username, passwordEncoder.encode(rawPassword), email, Role.PATIENT);
        userRepository.save(user);

        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setPhone(phone);
        patient.setUser(user);

        return patientRepository.save(patient);
    }

    public Optional<Patient> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(patientRepository::findByUser);
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Transactional
    public Patient updateProfile(Patient patient) {
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
