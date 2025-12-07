package com.example.laborator5.Domain;
import java.io.Serializable;
import java.util.Objects;

public class Patient implements IdentifiableInterface<Integer>, Serializable {
    private Integer PatientId;
    private String Name;
    private String problem;
    private String email;
    private String telephone;

    public Patient(Integer NewId, String NewName, String NewTelephone, String NewEmail, String NewProblem){
        PatientId = NewId;
        Name = NewName;
        telephone = NewTelephone;
        email = NewEmail;
        problem = NewProblem;
    }

    public String getName() {
        return Name;
    }

    public void setName(String newName){
        this.Name = newName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String newTelephone) {
        this.telephone = newTelephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String newProblem){
        this.problem = newProblem;
    }

    @Override
    public Integer getId() {
        return PatientId;
    }

    @Override
    public void setId(Integer newPatientID){
        this.PatientId = newPatientID;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Patient patient = (Patient) object;
        return PatientId.equals(patient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(PatientId, Name, problem, email, telephone);
    }

    @Override
    public String toString() {
        return "PatientId=" + PatientId +
                ", Name='" + Name + '\'' +
                ", problem='" + problem + '\'' +
                ", email='" + email + '\'' +
                ", telephone=" + telephone;
    }



}
