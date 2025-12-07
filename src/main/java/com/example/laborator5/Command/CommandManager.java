package com.example.laborator5.Command;

import com.example.laborator5.Domain.IdentifiableInterface;
import com.example.laborator5.Repository.IRepository;

import java.util.Stack;

public class CommandManager<ID, T extends IdentifiableInterface<ID>> {
    private final Stack<ICommand<ID, T>> undoStack = new Stack<>();
    private final Stack<ICommand<ID, T>> redoStack = new Stack<>();
    IRepository<ID, T> repository;

    public void executeCommand(ICommand<ID, T> command) {
        command.execute();

        undoStack.push(command);

        redoStack.clear();
        repository = command.getRepository();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            ICommand<ID, T> command = undoStack.pop();

            command.undo();

            redoStack.push(command);

            repository = command.getRepository();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            ICommand<ID, T> command = redoStack.pop();

            command.redo();

            undoStack.push(command);

            repository = command.getRepository();
        }
    }

    public boolean isUndoAvailable() {
        return !undoStack.isEmpty();
    }

    public boolean isRedoAvailable() {
        return !redoStack.isEmpty();
    }

    public IRepository<ID, T> getRepository(){
        return repository;
    }
}