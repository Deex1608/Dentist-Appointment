package com.example.laborator5.PatientTests;

import com.example.laborator5.Domain.Patient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    private Patient patient;

    private final Integer PATIENT_ID = 1;
    private final String PATIENT_NAME = "John Doe";
    private final String PATIENT_TELEPHONE = "0123456789";
    private final String PATIENT_EMAIL = "john@example.com";
    private final String PATIENT_PROBLEM = "Sensitivity";

    @BeforeEach
    void setUp() {
        patient = new Patient(PATIENT_ID, PATIENT_NAME, PATIENT_TELEPHONE, PATIENT_EMAIL, PATIENT_PROBLEM);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(PATIENT_ID, patient.getId());
        assertEquals(PATIENT_NAME, patient.getName());
        assertEquals(PATIENT_TELEPHONE, patient.getTelephone());
        assertEquals(PATIENT_EMAIL, patient.getEmail());
        assertEquals(PATIENT_PROBLEM, patient.getProblem());
    }

    @Test
    void testSetters() {
        patient.setName("Jane Doe");
        patient.setTelephone("9876543210");
        patient.setEmail("jane@example.com");
        patient.setProblem("Cavities");
        patient.setId(2);

        assertEquals(2, patient.getId());
        assertEquals("Jane Doe", patient.getName());
        assertEquals("9876543210", patient.getTelephone());
        assertEquals("jane@example.com", patient.getEmail());
        assertEquals("Cavities", patient.getProblem());
    }

    @Test
    void testEquals_SameId() {
        Patient samePatient = new Patient(1, "Other", "0007770000", "other@mail.com", "Broken Teeth");
        assertEquals(patient, samePatient);
    }

    @Test
    void testEquals_DifferentId() {
        Patient differentPatient = new Patient(2, "John Doe", "0123456789", "john@example.com", "Sensitivity");
        assertNotEquals(patient, differentPatient);
    }

    @Test
    void testEquals_NullOrDifferentClass() {
        assertNotEquals(patient, null);
        assertNotEquals(patient, "string");
    }

    @Test
    void testHashCodeConsistency() {
        int hash1 = patient.hashCode();
        int hash2 = patient.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testToStringContainsAllFields() {
        String patientToString = patient.toString();
        assertTrue(patientToString.contains("PatientId=1"));
        assertTrue(patientToString.contains("Name='John Doe'"));
        assertTrue(patientToString.contains("problem='Sensitivity'"));
        assertTrue(patientToString.contains("email='john@example.com'"));
        assertTrue(patientToString.contains("telephone=0123456789"));
    }

    @Test
    void testMutationChangesEquality() {
        Patient samePatient = new Patient(PATIENT_ID, PATIENT_NAME, PATIENT_TELEPHONE, PATIENT_EMAIL, PATIENT_PROBLEM);
        assertEquals(patient, samePatient);
        samePatient.setId(99);
        assertNotEquals(patient, samePatient);
    }
}
