package com.example.laborator5.Command;

import com.example.laborator5.Domain.IdentifiableInterface;
import com.example.laborator5.Repository.IRepository;

public interface ICommand<ID, T extends IdentifiableInterface<ID>> {
    void execute();
    void redo();
    void undo();
    IRepository<ID, T> getRepository();
}
