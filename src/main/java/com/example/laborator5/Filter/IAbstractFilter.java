package com.example.laborator5.Filter;


import com.example.laborator5.Domain.IdentifiableInterface;

public interface IAbstractFilter<T extends IdentifiableInterface> {
    boolean accept(T entity);
}
