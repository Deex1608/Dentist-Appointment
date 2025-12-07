package com.example.laborator5.Exceptions;

public class IllegalVariableType extends Exception {
    String message;
    public IllegalVariableType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
