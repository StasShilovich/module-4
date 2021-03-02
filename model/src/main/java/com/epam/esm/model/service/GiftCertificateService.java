package com.epam.esm.model.service;

import com.epam.esm.model.common.FilterParams;
import com.epam.esm.model.service.exception.IncorrectArgumentException;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;

import java.util.List;

public interface GiftCertificateService {

    CertificateDTO find(Long id) throws ServiceException, NotExistEntityException;

    CertificateDTO add(CertificateDTO certificateDTO) throws ServiceException;

    CertificateDTO update(CertificateDTO certificateDTO) throws ServiceException, NotExistEntityException;

    void delete(Long id) throws ServiceException, NotExistEntityException;

    List<CertificateDTO> filterByParameters(FilterParams filterParams, int page, int size)
            throws ServiceException, IncorrectArgumentException;

    long count() throws ServiceException;
}
