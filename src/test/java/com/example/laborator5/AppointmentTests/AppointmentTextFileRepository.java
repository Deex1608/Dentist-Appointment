package com.example.laborator5.AppointmentTests;

import com.example.laborator5.Repository.AppointmentTextFileRepository;

import com.example.laborator5.Domain.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTextFileRepositoryTest {

    private final String TEST_FILE = "data/test_appointments.csv";
    private final String TEST_DIRECTORY = "data";

    @BeforeEach
    void setupDir() {
        File directorFile = new File(TEST_DIRECTORY);
        if (!directorFile.exists()) {
            directorFile.mkdirs();
        }
    }

    public void setupTestFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write(content);
        }
    }

    @Test
    void testReadFromFile_InitialData() throws IOException {
        String initialData = "1,101,01/12/2025,09:00\n2,102,02/12/2025,10:00\n";
        setupTestFile(initialData);

        AppointmentTextFileRepository appointmentTextFileRepository = new AppointmentTextFileRepository(TEST_FILE);

        assertEquals(2, StreamSupport.stream(appointmentTextFileRepository.getAll().spliterator(), false).count());
        assertEquals(101, appointmentTextFileRepository.findById(1).getPatientId());
        assertEquals("10:00", appointmentTextFileRepository.findById(2).getAppointmentTime());
    }

    @Test
    void testReadFromFile_EmptyFile() {
        AppointmentTextFileRepository textFileRepository = new AppointmentTextFileRepository("data/empty_file.csv");
        assertEquals(0, StreamSupport.stream(textFileRepository.getAll().spliterator(), false).count());
    }

    @Test
    void testReadFromFile_InvalidLineSkipped() throws IOException {
        String invalidData = "1,101,01/12/2025,09:00\nBAD LINE, ONLY 2 TOKENS\n3,103,03/12/2025,11:00";
        setupTestFile(invalidData);

        AppointmentTextFileRepository textFileRepository = new AppointmentTextFileRepository(TEST_FILE);

        assertEquals(2, StreamSupport.stream(textFileRepository.getAll().spliterator(), false).count());
        assertNull(textFileRepository.findById(2));
    }

    @Test
    void testReadFromFile_NumberFormatException() throws IOException {
        String invalidData = "A,101,01/12/2025,09:00\n";
        setupTestFile(invalidData);

        assertThrows(RuntimeException.class, () -> {
            new AppointmentTextFileRepository(TEST_FILE);
        });
    }

    @Test
    void testAdd_WritesToFile() throws IOException {
        setupTestFile("");
        AppointmentTextFileRepository textFileRepository = new AppointmentTextFileRepository(TEST_FILE);
        Appointment newAppointment = new Appointment(3, 103, "03/12/2025", "11:00");
        textFileRepository.add(3, newAppointment);

        assertEquals(1, StreamSupport.stream(textFileRepository.getAll().spliterator(), false).count());
        AppointmentTextFileRepository newTextFileRepository = new AppointmentTextFileRepository(TEST_FILE);
        assertEquals(1, StreamSupport.stream(newTextFileRepository.getAll().spliterator(), false).count());
        assertEquals("03/12/2025", newTextFileRepository.findById(3).getAppointmentDate());
    }

    @Test
    void testDelete_WritesToFile() throws IOException {
        String initialData = "1,101,01/12/2025,09:00\n2,102,02/12/2025,10:00\n";
        setupTestFile(initialData);
        AppointmentTextFileRepository textFileRepository = new AppointmentTextFileRepository(TEST_FILE);
        textFileRepository.delete(1);
        assertNull(textFileRepository.findById(1));
        AppointmentTextFileRepository newTextFileRepository = new AppointmentTextFileRepository(TEST_FILE);
        assertNull(newTextFileRepository.findById(1));
        assertEquals(1, StreamSupport.stream(newTextFileRepository.getAll().spliterator(), false).count());
    }

    @Test
    void testWriteToFile_FormatCheck() throws IOException {
        setupTestFile("");
        AppointmentTextFileRepository textFileRepository = new AppointmentTextFileRepository(TEST_FILE);
        textFileRepository.add(5, new Appointment(5, 500, "20/11/2025", "14:30"));
        String fileContent = Files.readString(Paths.get(TEST_FILE));
        assertTrue(fileContent.contains("5,500,20/11/2025,14:30\n"));
    }
}