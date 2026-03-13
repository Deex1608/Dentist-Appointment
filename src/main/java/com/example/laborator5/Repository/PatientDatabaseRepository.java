package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDatabaseRepository implements IRepository<Integer, Patient> {
    protected String databaseURL;
    protected Connection databaseConnection = null;

    public PatientDatabaseRepository(String filePath) {
        this.databaseURL = "jdbc:sqlite:D:/University-Repo/Year-2/Semester-1/MAP/labs/RepoTemeMAP5/Laborator5/data/" +  filePath;
    }

    private void OpenConnection(){
        if (databaseConnection != null) {
            return;
        }

        try {
            databaseConnection = DriverManager.getConnection(databaseURL);
        }catch (SQLException sqlException){
            System.out.println("ERROR: " + sqlException.getMessage());
        }

    }

    private void CloseConnection(){
        try{
            if(databaseConnection == null || databaseConnection.isClosed()){
                return;
            }
            databaseConnection.close();
            databaseConnection = null;
        }catch(SQLException sqlException){
            System.out.println("ERROR: " + sqlException.getMessage());
        }
    }

    @Override
    public void add(Integer newGenericElementId, Patient newGenericElement) {
        this.OpenConnection();
        try(PreparedStatement preparedStatement = databaseConnection.prepareStatement("INSERT INTO patients(PatientID, Name, PhoneNumber, Email, Problem) VALUES (?, ?, ?, ?, ?)");){
            preparedStatement.setInt(1, newGenericElementId);
            preparedStatement.setString(2, newGenericElement.getName());
            preparedStatement.setString(3, newGenericElement.getTelephone());
            preparedStatement.setString(4, newGenericElement.getEmail());
            preparedStatement.setString(5, newGenericElement.getProblem());
            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected == 0){
                System.out.println("No patient was added ");
            }
            this.CloseConnection();
        }catch (SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public void delete(Integer genericElementId) {
        this.OpenConnection();
        try(PreparedStatement preparedStatement = databaseConnection.prepareStatement("DELETE FROM patients WHERE PatientID = ?")){
            preparedStatement.setInt(1, genericElementId);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected == 0){
                System.out.println("No patient found with ID " + genericElementId + " to delete");
            }
            this.CloseConnection();
        }catch (SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public void modify(Integer newGenericElementId, Patient newGenericElement) {
        this.OpenConnection();
        try(PreparedStatement preparedStatement = databaseConnection.prepareStatement("UPDATE patients SET Name = ?, PhoneNumber = ?, Email = ?, Problem = ? WHERE PatientID = ?")){
            preparedStatement.setString(1, newGenericElement.getName());
            preparedStatement.setString(2, newGenericElement.getTelephone());
            preparedStatement.setString(3, newGenericElement.getEmail());
            preparedStatement.setString(4, newGenericElement.getProblem());
            preparedStatement.setInt(5, newGenericElementId);
            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected == 0){
                System.out.println("No patient found with ID " + newGenericElementId + " to modify.");
            }
            this.CloseConnection();
        }catch (SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public Patient findById(Integer genericElementId) {
        this.OpenConnection();
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement("SELECT * FROM patients WHERE PatientID = ?")) {
            preparedStatement.setInt(1, genericElementId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int patientID = resultSet.getInt("PatientID");
                String name = resultSet.getString("Name");
                String phoneNumber = resultSet.getString("PhoneNumber");
                String email = resultSet.getString("Email");
                String problem = resultSet.getString("Problem");
                Patient foundPatient = new Patient(patientID, name, phoneNumber, email, problem);
                this.CloseConnection();
                resultSet.close();
                return foundPatient;
            }
            this.CloseConnection();
            resultSet.close();
            return null;
        } catch (SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public Iterable<Patient> getAll() {
        List<Patient> listOfPatients = new ArrayList<>();
        this.OpenConnection();
        try(PreparedStatement preparedStatement = databaseConnection.prepareStatement("SELECT * FROM patients");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                int id = resultSet.getInt("PatientID");
                String name = resultSet.getString("Name");
                String phoneNumber = resultSet.getString("PhoneNumber");
                String email = resultSet.getString("Email");
                String problem = resultSet.getString("Problem");
                Patient patientToBeAdded = new Patient(id, name, phoneNumber, email, problem);
                listOfPatients.add(patientToBeAdded);
            }
            this.CloseConnection();
            return listOfPatients;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        } catch (NullPointerException nullPointerException){
            throw new RuntimeException(nullPointerException);
        }
    }
}


// jdbc:sqlite:data/patientsdb.db