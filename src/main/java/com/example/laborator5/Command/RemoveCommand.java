package com.example.laborator5.Command;

import com.example.laborator5.Domain.IdentifiableInterface;
import com.example.laborator5.Repository.IRepository;

public class RemoveCommand<ID, T extends IdentifiableInterface<ID>> implements ICommand<ID, T> {
    private final IRepository<ID, T> genericRepository;
    private final T elementToRemove;
    private final ID elementID;

    public RemoveCommand(IRepository<ID, T> iRepository, ID newID, T newGeneric) {
        this.genericRepository = iRepository;
        this.elementToRemove = newGeneric;
        this.elementID = newID;
    }

    @Override
    public void execute() {
        genericRepository.delete(elementID);
    }

    @Override
    public void redo() {
        genericRepository.delete(elementID);
    }

    @Override
    public void undo() {
        genericRepository.add(elementID, elementToRemove);
    }
}
