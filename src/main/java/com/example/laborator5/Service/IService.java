package com.example.laborator5.Service;

import com.example.laborator5.Exceptions.IllegalVariableType;

public interface IService<ID> {
    boolean isInTheList(ID id) throws IllegalVariableType;
}
