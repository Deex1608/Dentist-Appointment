package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Domain.Patient;
import com.example.laborator5.Filter.IAbstractFilter;

import java.util.HashMap;

public class FilterRepositoryAppointment extends FilterRepository<Integer, Appointment> {
    private IAbstractFilter<Appointment> filter;

    public FilterRepositoryAppointment(IAbstractFilter<Appointment> filter) {
        super(filter);
        this.filter = filter;
    }

    @Override
    public Iterable<Appointment> getAll() {
        Iterable<Appointment> appointments = super.getAll();
        HashMap<Integer, Appointment> filteredAppointments = new HashMap<>();
        for (Appointment appointment : appointments) {
            if (filter.accept(appointment)) {
                filteredAppointments.put(appointment.getId(), appointment);
            }
        }
        return filteredAppointments.values();
    }
}
