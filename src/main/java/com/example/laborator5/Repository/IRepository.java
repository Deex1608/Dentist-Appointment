package com.example.laborator5.Repository;

import com.example.laborator5.Domain.IdentifiableInterface;

public interface IRepository<ID, T extends IdentifiableInterface<ID>> {
    void add(ID newGenericElementId, T newGenericElement);
    void delete(ID genericElementId);
    void modify(ID newGenericElementId, T newGenericElement);
    T findById(ID genericElementId);
    Iterable<T> getAll();
}
