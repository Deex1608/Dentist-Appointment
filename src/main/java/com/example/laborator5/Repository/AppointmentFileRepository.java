package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Appointment;

public abstract class AppointmentFileRepository extends MemoryRepository<Integer, Appointment> {
    protected String fileName;

    public AppointmentFileRepository(String fileName) {
        this.fileName = fileName;
        readFromFile();
    }

    protected abstract void readFromFile();

    protected abstract void writeToFile();

    @Override
    public void add(Integer AppointmentId, Appointment newAppointment){
        super.add(AppointmentId, newAppointment);
        writeToFile();
    }

    @Override
    public void delete(Integer AppointmentId){
        super.delete(AppointmentId);
        writeToFile();
    }
}
