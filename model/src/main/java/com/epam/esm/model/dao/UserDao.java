package com.epam.esm.model.dao;

import com.epam.esm.model.dao.entity.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User> {

    Optional<User> findByUsername(String username);
}
