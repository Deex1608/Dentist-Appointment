package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Patient;

public abstract class PacientFileRepository extends MemoryRepository<Integer, Patient> {
    protected String fileName;

    public PacientFileRepository(String fileName) {
        this.fileName = fileName;
        readFromFile();
    }

    protected abstract void readFromFile();

    protected abstract void writeToFile();

    @Override
    public void add(Integer PatientId, Patient patient){
        super.add(PatientId, patient);
        writeToFile();
    }

    @Override
    public void delete(Integer PatientId){
        super.delete(PatientId);
        writeToFile();
    }
}
