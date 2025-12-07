package com.example.laborator5.Filter;


import com.example.laborator5.Validation.ValidationVariables;
import com.example.laborator5.Domain.Patient;

public class FilterPatientByProblem implements IAbstractFilter<Patient> {

    String problem;

    public FilterPatientByProblem(String problem) {
        if(problem == null || ValidationVariables.hasSpecialCharacters(problem) || problem.isBlank()){
            throw new IllegalArgumentException("This is not a valid problem!");
        }
        this.problem = problem;
    }

    @Override
    public boolean accept(Patient isThisPatientEligibleForFilter) {
        if(isThisPatientEligibleForFilter.getProblem() == null || ValidationVariables.hasSpecialCharacters(isThisPatientEligibleForFilter.getProblem())
                || isThisPatientEligibleForFilter.getProblem().isBlank()){
            throw new IllegalArgumentException("This is not a valid problem!");
        }
        return this.problem.equals(isThisPatientEligibleForFilter.getProblem());
    }
}
