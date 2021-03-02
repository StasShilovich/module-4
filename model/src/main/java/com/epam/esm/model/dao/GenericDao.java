package com.epam.esm.model.dao;

import com.epam.esm.model.service.exception.NotExistEntityException;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {

    T create(T entity);

    Optional<T> findById(long id);

    T update(T entity);

    void delete(long id) throws NotExistEntityException;

    List<T> findAll(int offset, int limit);

    long getCountOfEntities();
}
