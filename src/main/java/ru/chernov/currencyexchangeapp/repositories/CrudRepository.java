package ru.chernov.currencyexchangeapp.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    Optional<T> findById(Long id);

    List<T> findAll();

    void save(T entity);

    void delete(Long id);

    void update(T entity);
}
