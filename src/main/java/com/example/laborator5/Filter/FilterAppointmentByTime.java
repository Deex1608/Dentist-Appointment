package com.example.laborator5.Filter;


import com.example.laborator5.Validation.ValidationVariables;
import com.example.laborator5.Domain.Appointment;

public class FilterAppointmentByTime implements IAbstractFilter<Appointment> {
    private String time;
    public FilterAppointmentByTime(String newTime) {
        if (!ValidationVariables.isATime(newTime)) {
            throw new IllegalArgumentException("Invalid time format! This must be of type hh:mm!");
        }
        this.time = newTime;
    }

    @Override
    public boolean accept(Appointment entity) {
        if (!ValidationVariables.isATime(entity.getAppointmentTime())) {
            throw new IllegalArgumentException("Invalid time format! This must be of type hh:mm!");
        }
        return this.time.equalsIgnoreCase(entity.getAppointmentTime());
    }
}
