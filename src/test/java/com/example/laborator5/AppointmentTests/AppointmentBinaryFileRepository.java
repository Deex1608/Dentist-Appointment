package com.example.laborator5.AppointmentTests;

import com.example.laborator5.Repository.AppointmentBinaryFileRepository;

import com.example.laborator5.Domain.Appointment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentBinaryFileRepositoryTest {

    private final String TEST_FILE = "test_appointments.bin";

    @AfterEach
    void tearDown() {
        File fileName = new File(TEST_FILE);
        if (fileName.exists()) {
            fileName.delete();
        }
    }

    private void setupTestFile(HashMap<Integer, Appointment> data) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
            objectOutputStream.writeObject(data);
        }
    }

    @Test
    void testReadFromFile_InitialData() throws IOException {
        HashMap<Integer, Appointment> initialData = new HashMap<>();
        initialData.put(1, new Appointment(1, 101, "01/12/2025", "09:00"));
        initialData.put(2, new Appointment(2, 102, "02/12/2025", "10:00"));
        setupTestFile(initialData);
        AppointmentBinaryFileRepository binaryFileRepository = new AppointmentBinaryFileRepository(TEST_FILE);
        assertEquals(2, StreamSupport.stream(binaryFileRepository.getAll().spliterator(), false).count());
        assertEquals(102, binaryFileRepository.findById(2).getPatientId());
    }

    @Test
    void testReadFromFile_EmptyFile() throws IOException {
        new File(TEST_FILE).createNewFile();
        AppointmentBinaryFileRepository binaryFileRepository = new AppointmentBinaryFileRepository(TEST_FILE);
        assertEquals(0, StreamSupport.stream(binaryFileRepository.getAll().spliterator(), false).count());
    }

    @Test
    void testReadFromFile_NonExistentFile() {
        AppointmentBinaryFileRepository binaryFileRepository = new AppointmentBinaryFileRepository(TEST_FILE);
        assertEquals(0, StreamSupport.stream(binaryFileRepository.getAll().spliterator(), false).count());
    }

    @Test
    void testAdd_WritesToFile() {
        AppointmentBinaryFileRepository binaryFileRepository = new AppointmentBinaryFileRepository(TEST_FILE);
        Appointment newAppointment = new Appointment(3, 103, "03/12/2025", "11:00");
        binaryFileRepository.add(3, newAppointment);
        assertEquals(1, StreamSupport.stream(binaryFileRepository.getAll().spliterator(), false).count());
        AppointmentBinaryFileRepository freshRepo = new AppointmentBinaryFileRepository(TEST_FILE);
        assertEquals(1, StreamSupport.stream(freshRepo.getAll().spliterator(), false).count(),
                "Data must persist to the binary file.");
        assertNotNull(freshRepo.findById(3));
    }

    @Test
    void testDelete_WritesToFile() throws IOException {
        HashMap<Integer, Appointment> initialData = new HashMap<>();
        initialData.put(1, new Appointment(1, 101, "01/12/2025", "09:00"));
        setupTestFile(initialData);
        AppointmentBinaryFileRepository binaryFileRepository = new AppointmentBinaryFileRepository(TEST_FILE);
        binaryFileRepository.delete(1);
        assertNull(binaryFileRepository.findById(1));
        AppointmentBinaryFileRepository newBinaryFileRepository = new AppointmentBinaryFileRepository(TEST_FILE);
        assertNull(newBinaryFileRepository.findById(1), "Deleted appointment must not be in the binary file.");
        assertEquals(0, StreamSupport.stream(newBinaryFileRepository.getAll().spliterator(), false).count());
    }
}