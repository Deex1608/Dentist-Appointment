package com.dentist.appointment;

import com.dentist.appointment.model.*;
import com.dentist.appointment.repository.AppointmentRepository;
import com.dentist.appointment.repository.DentistRepository;
import com.dentist.appointment.repository.PatientRepository;
import com.dentist.appointment.repository.UserRepository;
import com.dentist.appointment.service.AppointmentService;
import com.dentist.appointment.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DentistAppointmentApplicationTests {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DentistRepository dentistRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Dentist testDentist;
    private Patient testPatient;

    @BeforeEach
    void setUp() {
        testDentist = new Dentist("John", "Doe", "General Dentistry", "555-9999", "jdoe@test.com");
        dentistRepository.save(testDentist);

        testPatient = patientService.registerPatient(
                "testuser", "password123", "test@example.com",
                "Alice", "Smith", "555-0001");
    }

    @Test
    void contextLoads() {
        assertThat(patientService).isNotNull();
        assertThat(appointmentService).isNotNull();
    }

    @Test
    void registerPatient_createsUserAndPatient() {
        Optional<Patient> found = patientService.findByUsername("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Alice");
        assertThat(found.get().getLastName()).isEqualTo("Smith");
        assertThat(found.get().getUser().getRole()).isEqualTo(Role.PATIENT);
    }

    @Test
    void registerPatient_encodesPassword() {
        Optional<Patient> found = patientService.findByUsername("testuser");
        assertThat(found).isPresent();
        String storedPassword = found.get().getUser().getPassword();
        assertThat(passwordEncoder.matches("password123", storedPassword)).isTrue();
    }

    @Test
    void registerPatient_duplicateUsername_throwsException() {
        assertThatThrownBy(() ->
                patientService.registerPatient("testuser", "pass9999", "other@example.com",
                        "Bob", "Jones", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username already taken");
    }

    @Test
    void registerPatient_duplicateEmail_throwsException() {
        assertThatThrownBy(() ->
                patientService.registerPatient("anotheruser", "pass9999", "test@example.com",
                        "Bob", "Jones", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    void bookAppointment_savesCorrectly() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);

        Appointment appt = appointmentService.bookAppointment(
                testPatient, testDentist.getId(), date, time,
                "Routine Check-up", "First visit");

        assertThat(appt.getId()).isNotNull();
        assertThat(appt.getStatus()).isEqualTo(AppointmentStatus.PENDING);
        assertThat(appt.getDentist().getId()).isEqualTo(testDentist.getId());
        assertThat(appt.getPatient().getId()).isEqualTo(testPatient.getId());
    }

    @Test
    void bookAppointment_duplicateSlot_throwsException() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(11, 0);

        appointmentService.bookAppointment(testPatient, testDentist.getId(), date, time,
                "Teeth Cleaning", null);

        // Register another patient and try the same slot
        Patient otherPatient = patientService.registerPatient(
                "otherpatient", "password123", "other2@example.com",
                "Bob", "Brown", null);

        assertThatThrownBy(() ->
                appointmentService.bookAppointment(otherPatient, testDentist.getId(), date, time,
                        "Routine Check-up", null))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void cancelAppointment_byOwner_succeeds() {
        LocalDate date = LocalDate.now().plusDays(2);
        LocalTime time = LocalTime.of(9, 0);
        Appointment appt = appointmentService.bookAppointment(
                testPatient, testDentist.getId(), date, time, "Extraction", null);

        appointmentService.cancelAppointment(appt.getId(), testPatient);

        Appointment updated = appointmentRepository.findById(appt.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
    }

    @Test
    void cancelAppointment_byOtherPatient_throwsException() {
        LocalDate date = LocalDate.now().plusDays(2);
        LocalTime time = LocalTime.of(14, 0);
        Appointment appt = appointmentService.bookAppointment(
                testPatient, testDentist.getId(), date, time, "Filling", null);

        Patient otherPatient = patientService.registerPatient(
                "stranger", "password123", "stranger@example.com",
                "Tom", "Hardy", null);

        assertThatThrownBy(() ->
                appointmentService.cancelAppointment(appt.getId(), otherPatient))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    void updateStatus_changesAppointmentStatus() {
        LocalDate date = LocalDate.now().plusDays(3);
        LocalTime time = LocalTime.of(10, 30);
        Appointment appt = appointmentService.bookAppointment(
                testPatient, testDentist.getId(), date, time, "Crown", null);

        appointmentService.updateStatus(appt.getId(), AppointmentStatus.CONFIRMED);

        Appointment updated = appointmentRepository.findById(appt.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
    }

    @Test
    void getAppointmentsForPatient_returnsPatientAppointments() {
        LocalDate date = LocalDate.now().plusDays(5);
        appointmentService.bookAppointment(testPatient, testDentist.getId(),
                date, LocalTime.of(9, 0), "Check-up", null);
        appointmentService.bookAppointment(testPatient, testDentist.getId(),
                date, LocalTime.of(10, 0), "Cleaning", null);

        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(testPatient);
        assertThat(appointments).hasSize(2);
    }

    @Test
    void patient_fullName_returnsCorrectFormat() {
        assertThat(testPatient.getFullName()).isEqualTo("Alice Smith");
    }

    @Test
    void dentist_fullName_returnsCorrectFormat() {
        assertThat(testDentist.getFullName()).isEqualTo("Dr. John Doe");
    }

    @Test
    void updatePatientProfile_savesChanges() {
        testPatient.setAddress("123 Main St");
        testPatient.setMedicalHistory("No allergies");
        patientService.updateProfile(testPatient);

        Optional<Patient> updated = patientRepository.findById(testPatient.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getAddress()).isEqualTo("123 Main St");
        assertThat(updated.get().getMedicalHistory()).isEqualTo("No allergies");
    }

    @Test
    void findAll_patients_returnsList() {
        List<Patient> patients = patientService.findAll();
        assertThat(patients).isNotEmpty();
    }
}
