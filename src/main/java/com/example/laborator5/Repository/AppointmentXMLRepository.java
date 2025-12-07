package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Appointment;

import java.io.*;

public class AppointmentXMLRepository extends AppointmentFileRepository{

    public AppointmentXMLRepository(String XMLFileName){
        super(XMLFileName);
    }

    private int extractValue(String line, String objectTag) {
        String openTag = "<" + objectTag + ">";
        String closeTag = "</" + objectTag + ">";
        String tagValue = line.substring(openTag.length(), line.indexOf(closeTag));
        return Integer.parseInt(tagValue.trim());
    }

    private String extractString(String line, String objectTag) {
        String openTag = "<" + objectTag + ">";
        String closeTag = "</" + objectTag + ">";
        return line.substring(openTag.length(), line.indexOf(closeTag)).trim();
    }

    @Override
    protected void readFromFile() {
        File XMLFile = new File(fileName);
        if (!XMLFile.exists())
            return;

        try (BufferedReader XMLReader = new BufferedReader(new FileReader(XMLFile))) {

            String readLine;
            Appointment currentAppointment = null;

            while ((readLine = XMLReader.readLine()) != null) {
                readLine = readLine.trim();

                if (readLine.isEmpty() ||
                        readLine.equals("<appointments>") ||
                        readLine.equals("</appointments>") ||
                        readLine.equals("</patient>")) {
                    continue;
                }

                if (readLine.equals("<appointment>")) {
                    currentAppointment = new Appointment(0, 0, "", "");
                    continue;
                }

                if (currentAppointment == null) {
                    throw new Exception("Invalid XML structure: Found data tag or unknown line before <appointment> start.");
                }

                if (readLine.startsWith("<AppointmentID>")) {
                    currentAppointment.setId(extractValue(readLine, "AppointmentID"));
                }
                else if (readLine.startsWith("<PatientID>")) {
                    currentAppointment.setPatientId(extractValue(readLine, "PatientID"));
                }
                else if (readLine.startsWith("<Date>")) {
                    currentAppointment.setAppointmentDate(extractString(readLine, "Date"));
                }
                else if (readLine.startsWith("<Time>")) {
                    currentAppointment.setAppointmentTime(extractString(readLine, "Time"));
                }

                else if (readLine.equals("</appointment>")) {
                    this.listOfGenericElements.put(currentAppointment.getId(), currentAppointment);
                    currentAppointment = null;
                }
            }

        } catch (Exception thrownException) {
            throw new RuntimeException("Could not read XML file: ", thrownException);
        }
    }

    @Override
    protected void writeToFile() {
        try (BufferedWriter XMLWriter = new BufferedWriter(new FileWriter(fileName))) {

            XMLWriter.write("<appointments>\n");

            for (Appointment appointment : this.listOfGenericElements.values()) {
                XMLWriter.write("    <appointment>\n");
                XMLWriter.write("        <AppointmentID>" + appointment.getId() + "</AppointmentID>\n");
                XMLWriter.write("        <PatientID>" + appointment.getPatientId() + "</PatientID>\n");
                XMLWriter.write("        <Date>" + appointment.getAppointmentDate() + "</Date>\n");
                XMLWriter.write("        <Time>" + appointment.getAppointmentTime() + "</Time>\n");
                XMLWriter.write("    </appointment>\n");
            }

            XMLWriter.write("</appointments>\n");

        } catch (Exception thrownException) {
            throw new RuntimeException("Could not write XML file:", thrownException);
        }
    }
}
