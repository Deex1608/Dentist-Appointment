package com.example.laborator5.Domain;

import java.io.Serializable;
import java.util.Objects;

public class Appointment implements IdentifiableInterface<Integer>, Serializable {
    private Integer AppointmentId;
    private Integer PatientId;
    private String AppointmentDate;
    private String AppointmentTime;

    public Appointment(Integer AppointmentId, Integer newPatientId, String AppointmentDate, String AppointmentTime) {
        this.AppointmentId = AppointmentId;
        this.PatientId = newPatientId;
        this.AppointmentDate = AppointmentDate;
        this.AppointmentTime = AppointmentTime;
    }

    @Override
    public Integer getId() {
        return AppointmentId;
    }

    @Override
    public void setId(Integer newAppointmentID) {
        this.AppointmentId = newAppointmentID;
    }

    public Integer getPatientId() {
        return PatientId;
    }

    public void setPatientId(Integer newPatientId) {
        PatientId = newPatientId;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.AppointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return AppointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.AppointmentTime = appointmentTime;
    }

    @Override
    public String toString() {
        return "Appointment has" +
                "AppointmentId=" + AppointmentId +
                ", PatientId=" + PatientId +
                ", AppointmentDate= '" + AppointmentDate + '\'' +
                ", AppointmentTime= '" + AppointmentTime + '\'';
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Appointment that = (Appointment) object;
        return AppointmentId.equals(that.AppointmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(AppointmentId, PatientId, AppointmentDate, AppointmentTime);
    }


}
