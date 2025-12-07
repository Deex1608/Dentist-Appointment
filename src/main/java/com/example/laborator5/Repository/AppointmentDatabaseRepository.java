package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDatabaseRepository implements IRepository<Integer, Appointment> {
    protected String databaseURL;
    protected Connection databaseConnection = null;

    public AppointmentDatabaseRepository(String filePath) {
        this.databaseURL = "jdbc:sqlite:D:/Uni/Sem3/RepoTemeMAP5/Laborator5/data/" +  filePath;
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
            if(databaseConnection == null && databaseConnection.isClosed()){
                return;
            }
            databaseConnection.close();
            databaseConnection = null;
        }catch(SQLException sqlException){
            System.out.println("ERROR: " + sqlException.getMessage());
        }
    }

    @Override
    public void add(Integer newGenericElementId, Appointment newGenericElement) {
        this.OpenConnection();
        try(PreparedStatement preparedStatement = databaseConnection.prepareStatement("INSERT INTO appointments VALUES (?, ?, ?, ?)");){
            preparedStatement.setInt(1, newGenericElementId);
            preparedStatement.setInt(2, newGenericElement.getPatientId());
            preparedStatement.setString(3, newGenericElement.getAppointmentDate());
            preparedStatement.setString(4, newGenericElement.getAppointmentTime());
            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected == 0){
                System.out.println("No appointment was added ");
            }

            this.CloseConnection();
        }catch (SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public void delete(Integer genericElementId) {
        this.OpenConnection();
        try(PreparedStatement preparedStatement = databaseConnection.prepareStatement("DELETE FROM appointments WHERE ID = ?")){
            preparedStatement.setInt(1, genericElementId);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected == 0){
                System.out.println("No appointment found with ID " + genericElementId + " to delete");
            }
            this.CloseConnection();
        }catch (SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public void modify(Integer newGenericElementId, Appointment newGenericElement) {
        this.OpenConnection();
        try(PreparedStatement preparedStatement = databaseConnection.prepareStatement("UPDATE appointments SET  PatientID = ?, AppointmentDate = ?, AppointmentTime = ? WHERE ID = ?")){

            preparedStatement.setInt(1, newGenericElement.getPatientId());
            preparedStatement.setString(2, newGenericElement.getAppointmentDate());
            preparedStatement.setString(3, newGenericElement.getAppointmentTime());
            preparedStatement.setInt(4, newGenericElementId);
            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected == 0){
                System.out.println("No appointment found with ID " + newGenericElementId + " to modify.");
            }
            this.CloseConnection();
        }catch (SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public Appointment findById(Integer genericElementId) {
        this.OpenConnection();
        try(PreparedStatement preparedStatement = databaseConnection.prepareStatement("SELECT * FROM appointments WHERE ID = ?")) {
            preparedStatement.setInt(1, genericElementId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int AppointmentID = resultSet.getInt("ID");
                int PatientID = resultSet.getInt("PatientID");
                String appointmentDate = resultSet.getString("AppointmentDate");
                String appointmentTime = resultSet.getString("AppointmentTime");
                Appointment foundAppointment = new Appointment(AppointmentID, PatientID, appointmentDate, appointmentTime);
                this.CloseConnection();
                resultSet.close();
                return foundAppointment;
            }
            this.CloseConnection();
            resultSet.close();
            return null;
        } catch (SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public Iterable<Appointment> getAll() {
        List<Appointment> listOfAppointments = new ArrayList<>();
        this.OpenConnection();
        try(PreparedStatement preparedStatement = databaseConnection.prepareStatement("SELECT * FROM appointments");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                int AppointmentID = resultSet.getInt("ID");
                int PatientID = resultSet.getInt("PatientID");
                String appointmentDate = resultSet.getString("AppointmentDate");
                String appointmentTime = resultSet.getString("AppointmentTime");
                Appointment appointmentToBeAdded = new Appointment(AppointmentID, PatientID, appointmentDate, appointmentTime);
                listOfAppointments.add(appointmentToBeAdded);
            }
            this.CloseConnection();
            return listOfAppointments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}