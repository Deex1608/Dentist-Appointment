package com.example.laborator5.Validation;

import com.example.laborator5.Domain.Patient;
import com.example.laborator5.Exceptions.IllegalVariableType;
import com.example.laborator5.UI.CONSTANTS;

public class PatientValidation implements IValidation<Integer> {
    Patient verifyPatientObject;
    public PatientValidation(Integer PatientID, String PatientName, String PatientPhoneNumber, String PatientEmail, String Problem) throws IllegalVariableType {
        this.verifyPatientObject = new Patient(PatientID, PatientName, PatientPhoneNumber, PatientEmail, Problem);
        verifyPatient();
    }

    public boolean IsNotAGoodId(Integer PatientId) {
        return PatientId == null || PatientId < CONSTANTS.WE_CHECK_WITH_A_ZERO;
    }

    boolean IsNotAGoodName(String Name) {
        return (Name == null || Name.isBlank() || ValidationVariables.hasSpecialCharacters(Name) || ValidationVariables.hasNumbers(Name));
    }

    boolean IsNotAGoodPhoneNumber(String PhoneNumber) {
        return PhoneNumber == null || PhoneNumber.isBlank() || !ValidationVariables.isAPhoneNumber(PhoneNumber) || PhoneNumber.isBlank();
    }

    boolean IsNotAGoodEmail(String Email) {
        return Email == null || Email.isBlank() || !ValidationVariables.isAnEmail(Email);
    }

    boolean IsNotAGoodProblem(String Problem) {
        return Problem == null || ValidationVariables.hasSpecialCharacters(Problem) || Problem.isBlank();
    }

    public void verifyPatient() throws IllegalVariableType {
        if (IsNotAGoodId(verifyPatientObject.getId())){
            throw new IllegalVariableType("Please insert a valid ID! It should be an integer greater than 0!");
        }
        if (IsNotAGoodName(verifyPatientObject.getName())){
            throw new IllegalVariableType("Please insert a valid name! It should be a string!");
        }
        if (IsNotAGoodPhoneNumber(verifyPatientObject.getTelephone())){
            throw new IllegalVariableType("Please insert a valid phone number! It could be of type +40 777 777 777!");
        }
        if (IsNotAGoodEmail(verifyPatientObject.getEmail())){
            throw new IllegalVariableType("Please insert a valid email! It could be of type name.email@something.com!");
        }
        if (IsNotAGoodProblem(verifyPatientObject.getProblem())){
            throw new IllegalVariableType("Please insert a valid problem!");
        }
    }
}
