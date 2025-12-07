package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Patient;
import com.example.laborator5.UI.CONSTANTS;

import java.io.*;
import java.util.Iterator;

public class PatientTextFileRepository extends PacientFileRepository{
    public PatientTextFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected void readFromFile(){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(this.fileName))){
            String line = bufferedReader.readLine();
            while(line!=null){
                String[] tokens = line.split(",");
                if(tokens.length != CONSTANTS.WE_CHECK_IF_IS_FIVE){
                    line = bufferedReader.readLine();
                    continue;
                }
                Integer PatientId = Integer.parseInt(tokens[0].trim());
                String PatientName = tokens[1].trim();
                String PatientPhoneNumber = tokens[2].trim();
                String PatientEmail = tokens[3].trim();
                String PatientProblem = tokens[4].trim();
                Patient newPatient = new Patient(PatientId, PatientName, PatientPhoneNumber, PatientEmail, PatientProblem);
                listOfGenericElements.put(PatientId, newPatient);
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            throw new RuntimeException(fileNotFoundException);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    @Override
    protected void writeToFile(){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.fileName))){
            Iterable<Patient> patientIterable = this.getAll();
            for(Patient patient : patientIterable){
                bufferedWriter.write(patient.getId()+","+patient.getName()+","+patient.getTelephone()+","+patient.getEmail()+","+patient.getProblem()+"\n");
                bufferedWriter.newLine();
            }
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }
}
