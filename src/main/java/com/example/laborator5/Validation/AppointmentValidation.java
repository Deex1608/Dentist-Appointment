package com.example.laborator5.Validation;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Exceptions.IllegalVariableType;
import com.example.laborator5.UI.CONSTANTS;

public class AppointmentValidation implements IValidation<Integer>{
    Appointment verifyAppointmentObject;
    public AppointmentValidation(Integer AppointmentId, Integer PatientId, String Date, String Time) throws IllegalVariableType {
        this.verifyAppointmentObject = new Appointment(AppointmentId, PatientId, Date, Time);
        verifyAppointment();
    }

    @Override
    public boolean IsNotAGoodId(Integer AppointmentID) {
        return AppointmentID == null || AppointmentID < CONSTANTS.WE_CHECK_WITH_A_ZERO;
    }

    boolean IsNotAGoodPatientID(Integer PatientId) {
        return PatientId == null || PatientId < CONSTANTS.WE_CHECK_WITH_A_ZERO;
    }

    boolean IsNotAGoodTime(String Time) throws IllegalVariableType {
        return Time.isBlank() || !ValidationVariables.isATime(Time);
    }

    boolean IsNotAGoodDate(String Date) {
        return Date.isBlank() || !ValidationVariables.isADate(Date);
    }

    public void verifyAppointment() throws IllegalVariableType {
        if (IsNotAGoodId(verifyAppointmentObject.getId())) {
            throw new IllegalVariableType("This should be a valid Appointment id!");
        }
        if  (IsNotAGoodPatientID(verifyAppointmentObject.getPatientId())) {
            throw new IllegalArgumentException("Patient ID cannot be negative or zero!");
        }
        if (IsNotAGoodDate(verifyAppointmentObject.getAppointmentDate())) {
            throw new IllegalArgumentException("Appointment Date cannot be empty! Appointment Date: " + verifyAppointmentObject.getAppointmentDate());
        }
        if (IsNotAGoodTime(verifyAppointmentObject.getAppointmentTime())) {
            throw new IllegalArgumentException("Appointment Time cannot be null or empty!");
        }
    }
}
