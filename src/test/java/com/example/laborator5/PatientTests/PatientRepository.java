package com.example.laborator5.PatientTests;

import com.example.laborator5.Repository.MemoryRepository;

import com.example.laborator5.Domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientRepositoryTest {

    private MemoryRepository<Integer, Patient> patientMemoryRepository;
    private Patient patient;

    @BeforeEach
    void setUp() {
        patientMemoryRepository = new MemoryRepository<>();
        patient = new Patient(1, "John", "0777 666 777", "mail@test.com", "Sensitivity");
        patientMemoryRepository.add(patient.getId(), patient);
    }

    @Test
    void testAddAndGetAll() {
        assertTrue(patientMemoryRepository.getAll().iterator().hasNext());
    }

    @Test
    void testFindById_Found() {
        assertEquals(patient, patientMemoryRepository.findById(1));
    }

    @Test
    void testFindById_NotFound() {
        assertNull(patientMemoryRepository.findById(999));
    }

    @Test
    void testDelete_RemovesElement() {
        patientMemoryRepository.delete(1);
        assertNull(patientMemoryRepository.findById(1));
    }

    @Test
    void testModify_ReplacesElement() {
        Patient newPatient = new Patient(1, "Jane", "0744 999 111", "jane@mail.com", "Broken Teeth");
        patientMemoryRepository.modify(1, newPatient);
        Patient foundPatient = patientMemoryRepository.findById(1);
        assertEquals("Jane", foundPatient.getName());
    }
}
