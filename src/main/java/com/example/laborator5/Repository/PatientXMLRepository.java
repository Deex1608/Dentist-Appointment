package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Patient;

import java.io.*;

public class PatientXMLRepository extends PacientFileRepository{
    public PatientXMLRepository(String XMLFileName){
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
        File file = new File(fileName);
        if (!file.exists())
            return;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String readLine;
            Patient currentPatient = null;

            while ((readLine = bufferedReader.readLine()) != null) {
                readLine = readLine.trim();

                if (readLine.isEmpty() || readLine.equals("<patients>") || readLine.equals("</patients>")) {
                    continue;
                }

                if (readLine.equals("<patient>")) {
                    currentPatient = new Patient(0, "", "", "", "");
                    continue;
                }

                if (currentPatient == null) {
                    throw new Exception("Invalid XML structure: Found data tag before <patient> start.");
                }

                if (readLine.startsWith("<PatientID>")) {
                    currentPatient.setId(extractValue(readLine, "PatientID"));
                }
                else if (readLine.startsWith("<Name>")) {
                    currentPatient.setName(extractString(readLine, "Name"));
                }
                else if (readLine.startsWith("<Telephone>")) {
                    currentPatient.setTelephone(extractString(readLine, "Telephone"));
                }
                else if (readLine.startsWith("<Email>")) {
                    currentPatient.setEmail(extractString(readLine, "Email"));
                }
                else if (readLine.startsWith("<Problem>")) {
                    currentPatient.setProblem(extractString(readLine, "Problem"));
                }

                else if (readLine.equals("</patient>")) {
                    this.listOfGenericElements.put(currentPatient.getId(), currentPatient);
                    currentPatient = null;
                }
            }

        } catch (Exception thrownException) {
            throw new RuntimeException("Could not read XML file: ", thrownException);
        }
    }

    @Override
    protected void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            writer.write("<patients>\n");

            for (Patient currentPatient : this.listOfGenericElements.values()) {
                writer.write("    <patient>\n");
                writer.write("        <PatientID>" + currentPatient.getId() + "</PatientID>\n");
                writer.write("        <Name>" + currentPatient.getName() + "</Name>\n");
                writer.write("        <Telephone>" + currentPatient.getTelephone() + "</Telephone>\n");
                writer.write("        <Email>" + currentPatient.getEmail() + "</Email>\n");
                writer.write("        <Problem>" + currentPatient.getProblem() + "</Problem>\n");
                writer.write("    </patient>\n");
            }

            writer.write("</patients>\n");

        } catch (Exception thrownException) {
            throw new RuntimeException("Could not write XML file: ", thrownException);
        }
    }
}
