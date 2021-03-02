package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GenericDao;
import com.epam.esm.model.service.exception.NotExistEntityException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public abstract class GenericDaoImpl<T> implements GenericDao<T> {

    private final Class<T> clazz;

    @PersistenceContext
    protected EntityManager entityManager;

    public GenericDaoImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<T> findById(long id) {
        T t = entityManager.find(clazz, id);
        return Optional.ofNullable(t);
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(long id) throws NotExistEntityException {
        T t = entityManager.find(clazz, id);
        if (t != null) {
            entityManager.remove(t);
        } else {
            throw new NotExistEntityException("Entity with id=" + id + " not exist!");
        }
    }

    @Override
    public List<T> findAll(int offset, int limit) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);
        query.select(root);
        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public long getCountOfEntities() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(clazz)));
        return entityManager.createQuery(query).getSingleResult();
    }
}
