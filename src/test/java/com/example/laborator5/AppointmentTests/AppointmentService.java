package com.example.laborator5.AppointmentTests;

import com.example.laborator5.Service.AppointmentService;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Exceptions.IllegalVariableType;
import com.example.laborator5.Repository.IRepository;
import com.example.laborator5.Repository.MemoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceSimpleTest {

    private IRepository<Integer, Appointment> repository;
    private AppointmentService appointmentService;

    private final Integer ID_1 = 1;
    private final Integer PATIENT_ID_1 = 101;
    private final String DATE_1 = "10/11/2025";
    private final String TIME_1 = "10:00";

    private final Integer INVALID_ID = -5;
    private final String INVALID_DATE = "41/25/2025";

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository<>();
        appointmentService = new AppointmentService(repository);

        try {
            appointmentService.addAppointment(ID_1, PATIENT_ID_1, DATE_1, TIME_1);
            appointmentService.addAppointment(2, 102, "02/11/2025", "11:00");
        } catch (IllegalVariableType illegalVariableType) {
            fail("Setup failed: " + illegalVariableType.getMessage());
        }
    }

    @Test
    void testGetAllAppointments() {
        Iterable<Appointment> allAppointments = appointmentService.getAllAppointments();
        long countNumberOfAppointments = StreamSupport.stream(allAppointments.spliterator(), false).count();
        assertEquals(2, countNumberOfAppointments, "Should retrieve 2 initial appointments.");
    }

    @Test
    void testAddAppointment() {
        Integer newId = 3;

        try {
            appointmentService.addAppointment(newId, 103, "03/11/2025", "12:00");
        } catch (IllegalVariableType illegalVariableType) {
            fail("Add failed unexpectedly: " + illegalVariableType.getMessage());
        }

        assertNotNull(repository.findById(newId), "Appointment should be successfully added.");
        assertEquals(3, StreamSupport.stream(appointmentService.getAllAppointments().spliterator(), false).count());
    }

    @Test
    void testRemoveAppointment() {
        appointmentService.removeAppointment(ID_1);
        assertNull(repository.findById(ID_1), "Appointment 1 should be removed.");
        assertEquals(1, StreamSupport.stream(appointmentService.getAllAppointments().spliterator(), false).count());
    }

    @Test
    void testModifyAppointment() throws IllegalVariableType {
        String newDate = "01/01/2026";
        appointmentService.modifyAppointment(ID_1, 999, newDate, "08:00");
        Appointment updatedAppointment = repository.findById(ID_1);
        assertNotNull(updatedAppointment);
        assertEquals(newDate, updatedAppointment.getAppointmentDate(), "Appointment date should be updated.");
    }

    @Test
    void testFindByIdAnAppointment_Found() throws IllegalVariableType {
        Appointment foundAppointment = appointmentService.findByIdAnAppointment(ID_1);
        assertNotNull(foundAppointment);
        assertEquals(ID_1, foundAppointment.getId());
    }

    @Test
    void testFindByIdAnAppointment_NotFound() throws IllegalVariableType {
        Appointment foundAppointment = appointmentService.findByIdAnAppointment(999);
        assertNull(foundAppointment);
    }

    @Test
    void testFindByIdAnAppointment_InvalidId() {
        assertThrows(IllegalVariableType.class, () -> {
            appointmentService.findByIdAnAppointment(INVALID_ID);
        }, "Should throw for an invalid ID (<= 0).");
    }

    @Test
    void testFindByIdAnAppointment_NullId() {
        assertThrows(IllegalVariableType.class, () -> {
            appointmentService.findByIdAnAppointment(null);
        });
    }

    @Test
    void testIsInTheList_True() throws IllegalVariableType {
        assertTrue(appointmentService.isInTheList(ID_1));
    }

    @Test
    void testIsInTheList_False() throws IllegalVariableType {
        assertFalse(appointmentService.isInTheList(999));
    }

    @Test
    void testIsInTheList_InvalidId() {
        assertThrows(IllegalVariableType.class, () -> {
            appointmentService.isInTheList(INVALID_ID);
        }, "Should throw for an invalid ID.");
    }

    @Test
    void testFilterByAppointmentDate_Success() throws IllegalVariableType {
        appointmentService.addAppointment(3, 103, DATE_1, "12:00");
        Iterable<Appointment> filteredAAppointment = appointmentService.filterByAppointmentDate(DATE_1);
        assertNotNull(filteredAAppointment);
    }

    @Test
    void testFilterByAppointmentDate_InvalidDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.filterByAppointmentDate(INVALID_DATE);
        }, "Should throw for an invalid date format.");
    }

    @Test
    void testFilterByAppointmentDate_NullDate() {
        assertThrows(IllegalVariableType.class, () -> {
            appointmentService.filterByAppointmentDate(null);
        });
    }

    @Test
    void testFilterByAppointmentDate_BlankDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.filterByAppointmentDate("");
        });
    }

    @Test
    void testFilterByAppointmentTime_Success() throws IllegalVariableType {
        appointmentService.addAppointment(3, 103, "03/11/2025", TIME_1);
        Iterable<Appointment> filteredAppointment = appointmentService.filterByAppointmentTime(TIME_1);
        assertNotNull(filteredAppointment);
    }

    @Test
    void testFilterByAppointmentTime_InvalidTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.filterByAppointmentTime("10:90");
        }, "Should throw for an invalid time format.");
    }

    @Test
    void testFilterByAppointmentTime_BlankTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.filterByAppointmentTime("");
        });
    }

    @Test
    void testFilterByAppointmentTime_NullTime() {
        assertThrows(IllegalVariableType.class, () -> {
            appointmentService.filterByAppointmentTime(null);
        });
    }

    @Test
    void testAddAppointment_InvalidPatientId() {
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.addAppointment(3, -101, "02/11/2025", TIME_1);
        });
    }

    @Test
    void testAddAppointment_NullPatientId() {
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.addAppointment(3, null, "02/11/2025", TIME_1);
        });
    }
}