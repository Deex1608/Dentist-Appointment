package com.example.laborator5.Command;

import com.example.laborator5.Domain.IdentifiableInterface;
import com.example.laborator5.Repository.IRepository;

public class AddCommand<ID, T extends IdentifiableInterface<ID>> implements ICommand<ID, T> {
    private final IRepository<ID, T> genericRepository;
    private final T elementToAdd;
    private final ID elementID;

    public AddCommand(IRepository<ID, T> iRepository, ID newID, T newGeneric) {
        this.genericRepository = iRepository;
        this.elementToAdd = newGeneric;
        this.elementID = newID;
    }

    @Override
    public void execute() {
        genericRepository.add(elementID, elementToAdd);
    }

    @Override
    public void redo() {
        genericRepository.add(elementID, elementToAdd);
    }

    @Override
    public void undo() {
        genericRepository.delete(elementID);
    }
}
