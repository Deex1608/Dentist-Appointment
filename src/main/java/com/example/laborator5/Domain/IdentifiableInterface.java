package com.example.laborator5.Domain;

import com.example.laborator5.Exceptions.IllegalVariableType;

public interface IdentifiableInterface<ID> {
    public ID getId();

    void setId(ID newIID) throws IllegalVariableType;

}
