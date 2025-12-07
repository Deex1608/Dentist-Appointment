package com.example.laborator5.Repository;

import com.example.laborator5.Domain.IdentifiableInterface;

import java.util.HashMap;

public class MemoryRepository<ID, T extends IdentifiableInterface<ID>> implements IRepository<ID, T> {
    protected HashMap<ID, T> listOfGenericElements = new HashMap<>(); //This could be Patient or Appointment

    @Override
    public void add(ID newGenericElementId, T newGenericElement) {
        listOfGenericElements.putIfAbsent(newGenericElement.getId(), newGenericElement);
    }

    @Override
    public void delete(ID genericElementId) {
        listOfGenericElements.remove(genericElementId);
    }

    @Override
    public void modify(ID oldGenericElementId, T newGenericElement) {
        listOfGenericElements.put(oldGenericElementId, newGenericElement);
    }

    @Override
    public T findById(ID genericElementId) {
        for (T item: getAll()) {
            if  (item.getId().equals(genericElementId)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public Iterable<T> getAll() {
        return listOfGenericElements.values();
    }
}
