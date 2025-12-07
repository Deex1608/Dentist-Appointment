package com.example.laborator5.Command;

import com.example.laborator5.Domain.IdentifiableInterface;
import com.example.laborator5.Repository.IRepository;

public class UpdateCommand<ID, T extends IdentifiableInterface<ID>> implements ICommand<ID, T> {
    private final IRepository<ID, T> genericRepository;
    private final ID elementID;
    private final T elementToUpdate;
    private final T oldElement;

    public UpdateCommand(IRepository<ID, T> iRepository, ID newID, T newGeneric, T oldElement) {
        this.genericRepository = iRepository;
        this.elementToUpdate = newGeneric;
        this.elementID = newID;
        this.oldElement = oldElement;
    }

    @Override
    public void execute() {
        genericRepository.modify(elementID, elementToUpdate);
    }

    @Override
    public void redo() {
        genericRepository.modify(elementID, elementToUpdate);
    }

    @Override
    public void undo() {
        genericRepository.modify(elementID, oldElement);
    }

    @Override
    public IRepository<ID, T> getRepository() {
        return genericRepository;
    }
}
