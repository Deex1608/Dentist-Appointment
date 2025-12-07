package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.Domain.Patient;
import com.example.laborator5.UI.CONSTANTS;

import java.io.*;

public class AppointmentJSONRepository extends AppointmentFileRepository{
    public AppointmentJSONRepository(String JSONFileName){
        super(JSONFileName);
    }

    @Override
    protected void readFromFile() {
        File JSONFile = new File(fileName);

        if (!JSONFile.exists()){
            return;
        }

        if (!JSONFile.canRead()) {
            throw new RuntimeException("File exists, but cannot be read (Permission Denied).");
        }

        try(BufferedReader JSONReader = new BufferedReader(new FileReader(JSONFile))){
            String readLine;
            Appointment currentAppointment = null;
            boolean isFirstOpeningBrace = true;

            while((readLine = JSONReader.readLine()) != null){
                readLine = readLine.trim();

                if (readLine.equals("{") || readLine.equals("],") || readLine.equals("[") || readLine.startsWith("\"appointments\":")) {
                    currentAppointment = new Appointment(0, 0,  "", "");
                    if (readLine.equals("{") && isFirstOpeningBrace) {
                        isFirstOpeningBrace = false;
                    }
                    if (readLine.equals("[") && !isFirstOpeningBrace){
                        isFirstOpeningBrace = true;
                    }
                    continue;
                }

                if(readLine.startsWith("\"appointmentID\"") && currentAppointment != null){
                    int appointmentId  = Integer.parseInt(readLine.split(":", 2)[1].replace(",", "").trim());
                    currentAppointment.setId(appointmentId);
                } else if(readLine.startsWith("\"patientID\"") && currentAppointment != null){
                    int patientId  = Integer.parseInt(readLine.split(":", 2)[1].replace(",", "").trim());
                    currentAppointment.setPatientId(patientId);
                } else if(readLine.startsWith("\"date\"") && currentAppointment != null){
                    currentAppointment.setAppointmentDate(readLine.split(":", 2)[1].replace("\"", "").replace(",", "").trim());
                } else if (readLine.startsWith("\"time\"") && currentAppointment != null){
                    currentAppointment.setAppointmentTime(readLine.split(":", 2)[1].replace("\"", "").replace(",", "").trim());
                } else if (readLine.startsWith("}") && currentAppointment != null && !isFirstOpeningBrace) {
                    this.listOfGenericElements.put(currentAppointment.getId(), currentAppointment);
                    currentAppointment = null;
                }
            }

        }catch (IOException ioException){
            throw new RuntimeException("Could not read JSON file: " + fileName);
        } catch  (NumberFormatException numberFormatException) {
            throw new RuntimeException("Error parsing AppointmentID or PatientID to integer: ", numberFormatException);
        }
    }

    @Override
    protected void writeToFile() {
        try(BufferedWriter JSONWriter = new BufferedWriter(new FileWriter(fileName))){
            JSONWriter.write("{\n");
            JSONWriter.write("  \"Appointments\": [\n");

            int index = 0;
            for (Appointment appointment : this.listOfGenericElements.values()) {
                JSONWriter.write("    {\n");
                JSONWriter.write("      \"AppointmentID\": " + appointment.getId() + ",\n");
                JSONWriter.write("      \"PatientID\": \"" +appointment.getPatientId() + "\",\n");
                JSONWriter.write("      \"Date\": \"" + appointment.getAppointmentDate() + "\",\n");
                JSONWriter.write("      \"Time\": \"" + appointment.getAppointmentTime() + "\",\n");
                JSONWriter.write("    }");

                if (index < this.listOfGenericElements.size() - CONSTANTS.WE_CHECK_WITH_A_ONE)
                    JSONWriter.write(",\n");
                else
                    JSONWriter.write("\n");

                index++;
            }

            JSONWriter.write("  ]\n");
            JSONWriter.write("}\n");
        }catch (IOException ioException){
            throw new RuntimeException("Could not write JSON file: " + fileName);
        }
    }
}
