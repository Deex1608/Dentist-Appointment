package com.example.laborator5.AppointmentTests;

import com.example.laborator5.Domain.Appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {
    private Appointment appointment;
    private final Integer BASE_ID = 1;
    private final Integer BASE_PATIENT_ID = 1001;
    private final String BASE_DATE = "20/11/2025";
    private final String BASE_TIME = "14:30";
    @BeforeEach
    void setUp() {
        appointment = new Appointment(BASE_ID, BASE_PATIENT_ID, BASE_DATE, BASE_TIME);
    }
    @Test
    void testConstructorAndGetters() {
        assertEquals(BASE_ID, appointment.getId());
        assertEquals(BASE_PATIENT_ID, appointment.getPatientId());
        assertEquals(BASE_DATE, appointment.getAppointmentDate());
        assertEquals(BASE_TIME, appointment.getAppointmentTime());
    }

    @Test
    void testSetters() {
        appointment.setId(2);
        assertEquals(2, appointment.getId());
        appointment.setPatientId(10);
        assertEquals(10, appointment.getPatientId());
        appointment.setAppointmentDate("25/12/2025");
        assertEquals("25/12/2025", appointment.getAppointmentDate());
        appointment.setAppointmentTime("16:00");
        assertEquals("16:00", appointment.getAppointmentTime());
    }

    @Test
    void testEquals_SameObject() {
        assertEquals(appointment, appointment);
    }

    @Test
    void testEquals_SameId_DifferentDetails() {
        Appointment sameId = new Appointment(BASE_ID, 9999, "01/01/2026", "12:00");
        assertEquals(appointment, sameId, "Objects with the same ID must be equal.");
    }

    @Test
    void testEquals_DifferentId() {
        Appointment differentId = new Appointment(2, BASE_PATIENT_ID, BASE_DATE, BASE_TIME);
        assertNotEquals(appointment, differentId, "Objects with different IDs must not be equal.");
    }

    @Test
    void testEquals_NullOrDifferentClass() {
        assertNotEquals(appointment, null);
        assertNotEquals(appointment, "string", "Must not equal an object of a different class.");
    }

    @Test
    void testHashCodeConsistency() {
        int hash1 = appointment.hashCode();
        int hash2 = appointment.hashCode();
        assertEquals(hash1, hash2, "hashCode must be consistent.");
    }

    @Test
    void testHashCode_EqualsContract() {
        Appointment appointmentWithSameId = new Appointment(BASE_ID, 1001, "20/11/2025", "14:30");
        assertEquals(appointment.hashCode(), appointmentWithSameId.hashCode(), "If objects are equal, hash codes must be equal.");

        Appointment appointmentWithDifferentId = new Appointment(2, BASE_PATIENT_ID, BASE_DATE, BASE_TIME);
        assertNotEquals(appointment.hashCode(), appointmentWithDifferentId.hashCode(), "Different objects should ideally have different hash codes.");
    }

    @Test
    void testToStringContainsAllFields() {
        String appointmentString = appointment.toString();
        assertTrue(appointmentString.contains("AppointmentId=" + BASE_ID));
        assertTrue(appointmentString.contains("PatientId=" + BASE_PATIENT_ID));
        assertTrue(appointmentString.contains("AppointmentDate= '" + BASE_DATE + "'"));
        assertTrue(appointmentString.contains("AppointmentTime= '" + BASE_TIME + "'"));
        assertFalse(appointmentString.contains("Name="));
        assertFalse(appointmentString.contains("problem="));
        assertFalse(appointmentString.contains("email="));
        assertFalse(appointmentString.contains("telephone="));
    }

    @Test
    void testMutationChangesEquality() {
        Appointment sameAppointment = new Appointment(BASE_ID, BASE_PATIENT_ID, "01/01/2026", "12:00");
        assertEquals(appointment, sameAppointment);
        sameAppointment.setId(99);
        assertNotEquals(appointment, sameAppointment, "Changing the ID must break equality.");
    }
}