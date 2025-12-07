package com.example.laborator5.AppointmentTests;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Repository.MemoryRepository;

import com.example.laborator5.Domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentRepositoryTest {

    private MemoryRepository<Integer, Appointment> appointmentRepository;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointmentRepository = new MemoryRepository<>();
        appointment = new Appointment(1, 1001, "12/10/2026", "13:00");
        appointmentRepository.add(appointment.getId(), appointment);
    }

    @Test
    void testAddAndGetAll() {
        assertTrue(appointmentRepository.getAll().iterator().hasNext());
    }

    @Test
    void testFindById_Found() {
        assertEquals(appointment, appointmentRepository.findById(1));
    }

    @Test
    void testFindById_NotFound() {
        assertNull(appointmentRepository.findById(999));
    }

    @Test
    void testDelete_RemovesElement() {
        appointmentRepository.delete(1);
        assertNull(appointmentRepository.findById(1));
    }

    @Test
    void testModify_ReplacesElement() {
        Appointment newAppointment = new Appointment(1, 1002, "19/09/2025", "13:00");
        appointmentRepository.modify(1, newAppointment);
        Appointment foundAppointment = appointmentRepository.findById(1);
        assertEquals(1002, foundAppointment.getPatientId());
    }
}
