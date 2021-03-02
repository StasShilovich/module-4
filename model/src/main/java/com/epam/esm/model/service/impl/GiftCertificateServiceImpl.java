package com.epam.esm.model.service.impl;

import com.epam.esm.model.common.FilterParams;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.service.Page;
import com.epam.esm.model.service.exception.IncorrectArgumentException;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.converter.impl.GiftCertificateDTOMapper;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao certificateDao;
    private final GiftCertificateDTOMapper dtoMapper;

    public GiftCertificateServiceImpl(GiftCertificateDao certificateDao, GiftCertificateDTOMapper dtoMapper) {
        this.certificateDao = certificateDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public CertificateDTO find(Long id) throws ServiceException, NotExistEntityException {
        try {
            Optional<GiftCertificate> certificate = certificateDao.findById(id);
            return certificate.map(dtoMapper::toDTO)
                    .orElseThrow(() -> new NotExistEntityException("Gift certificate with id=" + id + " not exist!"));
        } catch (DataAccessException e) {
            log.error("Find certificate service exception", e);
            throw new ServiceException("Find certificate service exception", e);
        }
    }

    @Override
    public CertificateDTO add(CertificateDTO certificateDTO) throws ServiceException {
        try {
            GiftCertificate certificate = dtoMapper.fromDTO(certificateDTO);
            GiftCertificate giftCertificate = certificateDao.create(certificate);
            return dtoMapper.toDTO(giftCertificate);
        } catch (DataAccessException e) {
            log.error("Add certificate service exception", e);
            throw new ServiceException("Add certificate service exception", e);
        }
    }

    @Override
    public CertificateDTO update(CertificateDTO certificateDTO) throws ServiceException, NotExistEntityException {
        try {
            GiftCertificate certificate = dtoMapper.fromDTO(certificateDTO);
            GiftCertificate updated = certificateDao.update(certificate);
            return find(updated.getId());
        } catch (DataAccessException e) {
            log.error("Update certificate service exception", e);
            throw new ServiceException("Update certificate service exception", e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException, NotExistEntityException {
        try {
            certificateDao.delete(id);
        } catch (DataAccessException e) {
            log.error("Delete certificate service exception", e);
            throw new ServiceException("Delete certificate service exception", e);
        }
    }

    @Override
    public List<CertificateDTO> filterByParameters(FilterParams filterParams, int page, int size)
            throws ServiceException, IncorrectArgumentException {
        try {
            long count = count();
            Page certificatePage = new Page(page, size, count);
            List<GiftCertificate> certificates = certificateDao.filterByParameters(filterParams,
                    certificatePage.getOffset(), certificatePage.getLimit());
            return certificates.stream().map(dtoMapper::toDTO).collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Filter by parameters exception", e);
            throw new ServiceException("Filter by parameters exception", e);
        }
    }

    @Override
    public long count() throws ServiceException {
        try {
            return certificateDao.getCountOfEntities();
        } catch (DataAccessException e) {
            log.error("Count certificates service exception", e);
            throw new ServiceException("Count certificates service exception", e);
        }
    }
}
