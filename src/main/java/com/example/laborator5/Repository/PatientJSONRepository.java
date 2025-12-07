package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Patient;

import java.io.*;

public class PatientJSONRepository extends PacientFileRepository{
    public PatientJSONRepository(String JSONFileName){
        super(JSONFileName);
    }

    @Override
    protected void readFromFile() {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new RuntimeException("File not found at path: " + file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new RuntimeException("File exists, but cannot be read (Permission Denied).");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String readLine;
            Patient currentPatient = null;
            boolean isFirstOpeningBrace = true;

            while ((readLine = reader.readLine()) != null) {
                readLine = readLine.trim();

                if (readLine.equals("{") || readLine.equals("],") || readLine.equals("[") || readLine.startsWith("\"patients\":")) {
                    currentPatient = new Patient(0, "", "", "", "");
                    if (readLine.equals("{") && isFirstOpeningBrace) {
                        isFirstOpeningBrace = false;
                    }
                    if (readLine.equals("[") && !isFirstOpeningBrace){
                        isFirstOpeningBrace = true;
                    }
                    continue;
                }

                if (readLine.startsWith("\"PatientID\"") && currentPatient != null) {
                    int id = Integer.parseInt(readLine.split(":", 2)[1].replace(",", "").trim());
                    currentPatient.setId(id);
                } else if (readLine.startsWith("\"Name\"") && currentPatient != null) {
                    currentPatient.setName(readLine.split(":", 2)[1].replace("\"", "").replace(",", "").trim());
                } else if (readLine.startsWith("\"Phone\"") && currentPatient != null) {
                    currentPatient.setTelephone(readLine.split(":", 2)[1].replace("\"", "").replace(",", "").trim());
                } else if (readLine.startsWith("\"Email\"") && currentPatient != null) {
                    currentPatient.setEmail(readLine.split(":", 2)[1].replace("\"", "").replace(",", "").trim());
                } else if (readLine.startsWith("\"Problem\"") && currentPatient != null) {
                    currentPatient.setProblem(readLine.split(":", 2)[1].replace("\"", "").replace(",", "").trim());
                } else if (readLine.startsWith("}") && currentPatient != null && !isFirstOpeningBrace) {
                    this.listOfGenericElements.put(currentPatient.getId(), currentPatient);
                    currentPatient = null;
                }
            }

        } catch (IOException ioException) {
            throw new RuntimeException("Could not read JSON file: ", ioException);
        } catch (NumberFormatException numberFormatException) {
            throw new RuntimeException("Error parsing PatientID to integer: ", numberFormatException);
        }
    }


    @Override
    protected void writeToFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write("{\n");
            bufferedWriter.write("  \"patients\": [\n");

            int i = 0;
            for (Patient currentPatient : this.listOfGenericElements.values()) {
                bufferedWriter.write("    {\n");
                bufferedWriter.write("      \"PatientID\": " + currentPatient.getId() + ",\n");
                bufferedWriter.write("      \"Name\": \"" + currentPatient.getName() + "\",\n");
                bufferedWriter.write("      \"Phone\": \"" + currentPatient.getTelephone() + "\",\n");
                bufferedWriter.write("      \"Email\": \"" + currentPatient.getEmail() + "\",\n");
                bufferedWriter.write("      \"Problem\": \"" + currentPatient.getProblem() + "\"\n");
                bufferedWriter.write("    }");

                if (i < this.listOfGenericElements.size() - 1)
                    bufferedWriter.write(",\n");
                else
                    bufferedWriter.write("\n");

                i++;
            }

            bufferedWriter.write("  ]\n");
            bufferedWriter.write("}\n");
        } catch (IOException ioException) {
            throw new RuntimeException("Could not write JSON file: " + ioException);
        }
    }
}
