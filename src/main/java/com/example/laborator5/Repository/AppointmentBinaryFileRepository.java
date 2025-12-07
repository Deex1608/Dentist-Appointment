package com.example.laborator5.Repository;

import com.example.laborator5.Domain.Appointment;
import com.example.laborator5.UI.CONSTANTS;

import java.io.*;
import java.util.HashMap;

public class AppointmentBinaryFileRepository extends AppointmentFileRepository{
    public AppointmentBinaryFileRepository(String BinaryFileName) {
        super(BinaryFileName);
    }

    @Override
    protected void readFromFile() {
        File fileName = new File(this.fileName);
        if (!fileName.exists() || fileName.length() == CONSTANTS.WE_CHECK_WITH_A_ZERO) {
            this.listOfGenericElements = new HashMap<>();
            return;
        }
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(this.fileName))){
            this.listOfGenericElements= (java.util.HashMap<Integer, Appointment>) inputStream.readObject();

        } catch (FileNotFoundException | ClassNotFoundException runtimeException) {
            throw new RuntimeException(runtimeException);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

    }

    @Override
    protected void writeToFile() {
        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(this.fileName))){
            outputStream.writeObject(this.listOfGenericElements);

        } catch (FileNotFoundException fileNotFoundException) {
            throw new RuntimeException(fileNotFoundException);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }


    }
}
