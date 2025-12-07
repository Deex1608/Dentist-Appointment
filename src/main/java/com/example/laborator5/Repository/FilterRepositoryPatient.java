package com.example.laborator5.Repository;


import com.example.laborator5.Domain.Patient;
import com.example.laborator5.Filter.IAbstractFilter;

import java.util.HashMap;

public class FilterRepositoryPatient extends FilterRepository<Integer, Patient> {
    private IAbstractFilter<Patient> filter;

    public FilterRepositoryPatient(IAbstractFilter<Patient> filter) {
        super(filter);
        this.filter = filter;
    }

    @Override
    public Iterable<Patient> getAll() {
        Iterable<Patient> patients = super.getAll();
        HashMap<Integer, Patient> filteredPatients = new HashMap<>();
        for (Patient patient : patients) {
            if (filter.accept(patient)) {
                filteredPatients.put(patient.getId(), patient);
            }
        }
        return filteredPatients.values();
    }
}
