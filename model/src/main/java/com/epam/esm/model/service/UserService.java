package com.epam.esm.model.service;

import com.epam.esm.model.service.dto.UserDTO;
import com.epam.esm.model.service.exception.IncorrectArgumentException;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.exception.ServiceException;

import java.util.List;

public interface UserService {

    UserDTO find(Long id) throws ServiceException, NotExistEntityException;

    List<UserDTO> findAll(int page, int size) throws ServiceException, IncorrectArgumentException;

    long count() throws ServiceException;
}
