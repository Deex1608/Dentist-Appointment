package com.example.laborator5.Filter;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Validation.ValidationVariables;

public class FilterAppointmentByDate implements IAbstractFilter<Appointment> {
    String date;

    public FilterAppointmentByDate(String newDate) {
        if(!ValidationVariables.isADate(newDate)) {
            throw  new IllegalArgumentException("Invalid date format! This must be of type dd/mm/yyyy!");
        }
        this.date = newDate;
    }

    @Override
    public boolean accept(Appointment entity) {
        if(!ValidationVariables.isADate(entity.getAppointmentDate())) {
            throw  new IllegalArgumentException("Invalid date format! This must be of type dd/mm/yyyy!");
        }
        return this.date.equalsIgnoreCase(entity.getAppointmentDate());
    }
}
