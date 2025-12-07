package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Domain.Patient;
import com.example.laborator5.UI.CONSTANTS;

import java.io.*;

public class AppointmentTextFileRepository extends AppointmentFileRepository{
    public AppointmentTextFileRepository(String textFileName) {
        super(textFileName);
    }

    @Override
    protected void readFromFile(){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(this.fileName))){
            String line = bufferedReader.readLine();
            while(line!=null){
                String[] tokens = line.split(",");
                if(tokens.length != CONSTANTS.WE_CHECK_IF_IS_FOUR){
                    line = bufferedReader.readLine();
                    continue;
                }
                Integer AppointmentId = Integer.parseInt(tokens[0].trim());
                Integer PatientId = Integer.parseInt(tokens[1].trim());
                String Date = tokens[2].trim();
                String Time = tokens[3].trim();
                Appointment newAppointment = new Appointment(AppointmentId, PatientId, Date, Time);
                listOfGenericElements.put(AppointmentId, newAppointment);
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException foundException) {
            throw new RuntimeException(foundException);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    @Override
    protected void writeToFile(){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.fileName))){
            Iterable<Appointment> appointmentIterable = this.getAll();
            for(Appointment appointment : appointmentIterable){
                bufferedWriter.write(appointment.getId()+","+appointment.getPatientId()+","+appointment.getAppointmentDate()+","+appointment.getAppointmentTime()+"\n");
                bufferedWriter.newLine();
            }
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }
}
