package com.example.laborator5.Service;

import com.example.laborator5.Exceptions.IllegalVariableType;
import com.example.laborator5.Domain.Patient;
import com.example.laborator5.Filter.FilterPatientByName;
import com.example.laborator5.Filter.FilterPatientByProblem;
import com.example.laborator5.Repository.FilterRepositoryPatient;
import com.example.laborator5.Repository.IRepository;
import com.example.laborator5.Validation.PatientValidation;
import com.example.laborator5.Validation.ValidationVariables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PatientService implements IService<Integer> {
    private IRepository<Integer, Patient> patientRepository;

    public PatientService(IRepository<Integer, Patient> newPatientRepository) {
        this.patientRepository = newPatientRepository;
    }
    public Iterable<Patient> getAllPatients() {
        return patientRepository.getAll();
    }

    public void addPatient(Integer PatientId, String PatientName, String PatientTelephone, String PatientEmail, String PatientProblem) throws IllegalVariableType {
        PatientValidation patientToBeValidate =  new PatientValidation(PatientId, PatientName, PatientTelephone, PatientEmail, PatientProblem);
        Patient NewPatient =  new Patient(PatientId, PatientName, PatientTelephone, PatientEmail, PatientProblem);
        patientRepository.add(PatientId, NewPatient);
    }

    public void removePatient(Integer PatientId) throws IllegalVariableType {
        if(ValidationVariables.IsNotAGoodId(PatientId)) {
            throw new IllegalVariableType("Invalid ID! Please enter a natural number!");
        }
        //return patientRepository.delete(PatientId);
        patientRepository.delete(PatientId);
    }

    public void updatePatient(Integer OldPatientId, String PatientName, String PatientTelephone, String PatientEmail, String PatientProblem) throws IllegalVariableType {
        PatientValidation patientToBeValidate =  new PatientValidation(OldPatientId, PatientName, PatientTelephone, PatientEmail, PatientProblem);
        Patient NewPatient =  new Patient(OldPatientId, PatientName, PatientTelephone, PatientEmail, PatientProblem);
        //return patientRepository.updatePatient(NewPatient, OldPatientId);
        patientRepository.modify(OldPatientId, NewPatient);
    }

    @Override
    public boolean isInTheList(Integer PatientId) throws IllegalVariableType {
        if(ValidationVariables.IsNotAGoodId(PatientId)) {
            throw new IllegalVariableType("Invalid ID! Please enter a natural number!");
        }
        return findByIdAPatient(PatientId) != null;
    }

    public Patient findByIdAPatient(Integer patientId) throws IllegalVariableType {
        if(ValidationVariables.IsNotAGoodId(patientId)) {
            throw new IllegalVariableType("Invalid ID! Please enter a natural number!");
        }
        return patientRepository.findById(patientId);
    }

    public Iterable<Patient> filterByName(String name) {
        FilterPatientByName NameFilter = new FilterPatientByName(name);
        FilterRepositoryPatient filterRepositoryPatient = new FilterRepositoryPatient(NameFilter);
        for (Patient patient : patientRepository.getAll()) {
            filterRepositoryPatient.add(patient.getId(), patient);
        }
        return filterRepositoryPatient.getAll();
    }

    public Iterable<Patient> filterByProblem(String problem) {
        FilterPatientByProblem NameFilter = new FilterPatientByProblem(problem);
        FilterRepositoryPatient filterRepositoryPatient = new FilterRepositoryPatient(NameFilter);
        for (Patient patient : patientRepository.getAll()) {
            filterRepositoryPatient.add(patient.getId(), patient);
        }
        return filterRepositoryPatient.getAll();
    }

    // -----------> Java 8 Stream Reports
    private List<Patient> getPatientList(){
        Iterable<Patient> patientIterable = this.getAllPatients();
        return StreamSupport.stream(patientIterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<String> filterByAGivenProblemAndAnEmailDomain(String Problem, String emailDomain){
        List<Patient> allPatients = this.getPatientList();
        return allPatients.stream()
                .filter(patient -> patient.getProblem().equals(Problem) && patient.getEmail().contains(emailDomain))
                .map(patient -> patient.getName())
                .collect(Collectors.toList());
    }

    private String categorizeProblem(String problem) {
        problem = problem.toLowerCase();
        if (problem.contains("cavities") || problem.contains("gingivitis")) {
            return "Infection/Disease";
        } else if (problem.contains("malocclusion")) {
            return "Orthodontic";
        } else if (problem.contains("broken") || problem.contains("sensitivity")) {
            return "Acute/Structural";
        }
        return "Other";
    }

    public Map<String, List<Patient>>  groupPatientsByProblem(){
        List<Patient> allPatients = this.getPatientList();
        return allPatients.stream()
                .collect(Collectors.groupingBy(patient -> categorizeProblem(patient.getProblem())));
    }

    public List<String> filterByEmailDomain(String emailDomain){
        List<Patient> allPatients = this.getPatientList();
        return allPatients.stream()
                .filter(patient -> patient.getEmail().contains(emailDomain))
                .map(patient -> patient.getName())
                .collect(Collectors.toList());
    }
}
