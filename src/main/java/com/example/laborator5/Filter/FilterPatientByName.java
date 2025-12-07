package com.example.laborator5.Filter;

import com.example.laborator5.Validation.ValidationVariables;
import com.example.laborator5.Domain.Patient;

public class FilterPatientByName implements IAbstractFilter<Patient> {
    String name;

    public FilterPatientByName(String newName) {
        if (newName == null || newName.isBlank() || ValidationVariables.hasSpecialCharacters(newName) || ValidationVariables.hasNumbers(newName)) {
            throw new IllegalArgumentException("Name is blank or has an invalid type!");
        }
        this.name = newName;
    }

    @Override
    public boolean accept(Patient isThisPatientEligibleForFilter) {
        if (isThisPatientEligibleForFilter.getName() == null || isThisPatientEligibleForFilter.getName().isBlank()
                || ValidationVariables.hasSpecialCharacters(isThisPatientEligibleForFilter.getName())
                || ValidationVariables.hasNumbers(isThisPatientEligibleForFilter.getName())) {
            throw new IllegalArgumentException("Name is blank or has an invalid type!");
        }
        return this.name.equals(isThisPatientEligibleForFilter.getName());
    }
}
