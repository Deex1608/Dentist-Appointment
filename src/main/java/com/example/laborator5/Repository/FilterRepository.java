package com.example.laborator5.Repository;

import com.example.laborator5.Domain.IdentifiableInterface;
import com.example.laborator5.Filter.IAbstractFilter;

import java.util.HashMap;

public class FilterRepository<ID, T extends IdentifiableInterface<ID>> extends MemoryRepository<ID, T> {
    private IAbstractFilter<T> filter;

    public FilterRepository(IAbstractFilter<T> filter) {
        this.filter = filter;
    }

    @Override
    public Iterable<T> getAll() {
        Iterable<T> elements = super.getAll();
        HashMap<ID, T> filteredElements = new HashMap<>();
        for (T  element : elements) {
            if (filter.accept(element)) {
                filteredElements.put(element.getId(), element);
            }
        }
        return filteredElements.values();
    }
}
