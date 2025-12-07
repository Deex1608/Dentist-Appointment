package com.example.laborator5.Command;

import com.example.laborator5.Domain.IdentifiableInterface;

public interface ICommand<ID, T extends IdentifiableInterface<ID>> {
    void execute();
    void redo();
    void undo();
}
