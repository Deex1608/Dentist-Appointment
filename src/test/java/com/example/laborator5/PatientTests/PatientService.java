package com.example.laborator5.PatientTests;

import com.example.laborator5.Service.PatientService;

import com.example.laborator5.Domain.Patient;
import com.example.laborator5.Exceptions.IllegalVariableType;
import com.example.laborator5.Repository.MemoryRepository;
import com.example.laborator5.Repository.IRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class PatientServiceSimpleTest {

    private IRepository<Integer, Patient> repository;
    private PatientService patientService;

    private final Integer PATIENT_ID_1 = 1;
    private final Integer PATIENT_ID_2 = 2;
    private final String PATIENT_NAME_1 = "Alice";
    private final String PATIENT_PROBLEM_1 = "Headache";
    private final String PATIENT_PHONE_NUMBER = "111";
    private final String PATIENT_EMAIL = "a@b.com";

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository<>();
        patientService = new PatientService(repository);
        try {
            patientService.addPatient(PATIENT_ID_1, PATIENT_NAME_1, PATIENT_PHONE_NUMBER, PATIENT_EMAIL, PATIENT_PROBLEM_1);
            patientService.addPatient(PATIENT_ID_2, "Bob", PATIENT_PHONE_NUMBER, PATIENT_EMAIL, "Fever");
        } catch (IllegalVariableType e) {
            fail("Setup failed due to validation exception: " + e.getMessage());
        }
    }

    @Test
    void testGetAllPatients() {
        Iterable<Patient> allPatients = patientService.getAllPatients();
        long countNumberOfPatients = StreamSupport.stream(allPatients.spliterator(), false).count();
        assertEquals(2, countNumberOfPatients, "Should retrieve 2 initial patients.");
    }

    @Test
    void testAddPatient() {
        Integer newId = 3;
        try {
            patientService.addPatient(newId, "Charlie", PATIENT_PHONE_NUMBER, PATIENT_EMAIL, "Cough");
        } catch (IllegalVariableType e) {
            fail("Add failed unexpectedly.");
        }
        Patient addedPatient = repository.findById(newId);
        assertNotNull(addedPatient, "Patient should be successfully added.");
        assertEquals("Charlie", addedPatient.getName());
    }

    @Test
    void testRemovePatient_Success() throws IllegalVariableType {
        patientService.removePatient(PATIENT_ID_1);
        assertNull(repository.findById(PATIENT_ID_1), "Patient 1 should be removed from the repository.");
        long count = StreamSupport.stream(patientService.getAllPatients().spliterator(), false).count();
        assertEquals(1, count);
    }

    @Test
    void testRemovePatient_InvalidId() {
        Integer invalidId = -1;
        assertThrows(IllegalVariableType.class, () -> {
            patientService.removePatient(invalidId);
        }, "Should throw IllegalVariableType for an invalid ID.");
        assertNotNull(repository.findById(PATIENT_ID_1));
    }

    @Test
    void testUpdatePatient() throws IllegalVariableType {
        String newProblem = "Migraine";
        patientService.updatePatient(PATIENT_ID_1, PATIENT_NAME_1, "999", "new@email.com", newProblem);
        Patient updatedPatient = repository.findById(PATIENT_ID_1);
        assertNotNull(updatedPatient);
        assertEquals(newProblem, updatedPatient.getProblem(), "Problem should be updated.");
    }

    @Test
    void testFindByIdAPatient_Found() throws IllegalVariableType {
        Patient patientFound = patientService.findByIdAPatient(PATIENT_ID_2);
        assertNotNull(patientFound);
        assertEquals(PATIENT_ID_2, patientFound.getId());
    }

    @Test
    void testFindByIdAPatient_NotFound() throws IllegalVariableType {
        Patient patientFound = patientService.findByIdAPatient(999);
        assertNull(patientFound);
    }

    @Test
    void testFindByIdAPatient_InvalidId() {
        assertThrows(IllegalVariableType.class, () -> {
            patientService.findByIdAPatient(-1);
        }, "Should throw for zero ID.");
    }

    @Test
    void testIsInTheList_True() throws IllegalVariableType {
        assertTrue(patientService.isInTheList(PATIENT_ID_1));
    }

    @Test
    void testIsInTheList_False() throws IllegalVariableType {
        assertFalse(patientService.isInTheList(999));
    }

    @Test
    void testIsInTheList_InvalidId() {
        assertThrows(IllegalVariableType.class, () -> {
            patientService.isInTheList(-10);
        }, "Should throw for negative ID.");
    }

    @Test
    void testFilterByName_CallsGetAll() {
        Iterable<Patient> filtered = patientService.filterByName("AnyName");
        assertNotNull(filtered);
    }

    @Test
    void testFilterByProblem_CallsGetAll() {
        Iterable<Patient> filtered = patientService.filterByProblem("AnyProblem");
        assertNotNull(filtered);
    }
}