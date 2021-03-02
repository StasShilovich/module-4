package com.epam.esm.model.service.impl;

import com.epam.esm.model.common.FilterParams;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.common.SortType;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.converter.impl.GiftCertificateDTOMapper;
import com.epam.esm.model.service.converter.impl.TagDTOMapper;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.IncorrectArgumentException;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class GiftCertificateServiceImplTest {

    private static final String NAME = "Group meditation sessions";
    private static final String DESCRIPTION = "Include 30 minute guided meditation and empower hour";
    private static final String PRICE = "100";
    private static final String CREATE_DATE = "2021-01-12T15:15:00.0";
    private static final String LAST_UPDATE_DATE = "2021-01-14T13:10:00.0";

    @Mock
    GiftCertificateDao dao;
    GiftCertificateService service;
    GiftCertificateDTOMapper certificateDTOMapper;
    TagDTOMapper tagDTOMapper;
    GiftCertificate certificate;
    CertificateDTO certificateDTO;
    List<GiftCertificate> certificates;

    @BeforeEach
    public void setUp() {
        tagDTOMapper = new TagDTOMapper();
        certificateDTOMapper = new GiftCertificateDTOMapper(tagDTOMapper);
        service = new GiftCertificateServiceImpl(dao, certificateDTOMapper);
        certificate = GiftCertificate.builder()
                .id(1L)
                .name(NAME)
                .description(DESCRIPTION)
                .price(new BigDecimal(PRICE))
                .duration(2)
                .createDate(LocalDateTime.parse(CREATE_DATE))
                .lastUpdateDate(LocalDateTime.parse(LAST_UPDATE_DATE))
                .build();
        certificateDTO = CertificateDTO.builder()
                .id(1L)
                .name(NAME)
                .description(DESCRIPTION)
                .price(new BigDecimal(PRICE))
                .duration(2)
                .createDate(CREATE_DATE)
                .lastUpdateDate(LAST_UPDATE_DATE)
                .build();
        certificates = new ArrayList<>();
        certificates.add(certificate);
    }

    @Test
    void testFindPositive() throws NotExistEntityException, ServiceException {
        lenient().when(dao.findById(anyLong())).thenReturn(Optional.of(certificate));
        CertificateDTO actual = service.find(1L);
        assertEquals(certificateDTO, actual);
    }

    @Test
    void testFindNegative() throws NotExistEntityException, ServiceException {
        lenient().when(dao.findById(anyLong())).thenReturn(Optional.of(certificate));
        CertificateDTO actual = service.find(1L);
        assertNotEquals(null, actual);
    }

    @Test
    void testFindServiceException() {
        lenient().when(dao.findById(anyLong())).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(ServiceException.class, () -> service.find(1L));
    }

    @Test
    void testFindNotExistEntityException() {
        lenient().when(dao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotExistEntityException.class, () -> service.find(1L));
    }

    @Test
    void testAddPositive() throws ServiceException {
        lenient().when(dao.create(any(GiftCertificate.class))).thenReturn(certificate);
        CertificateDTO actual = service.add(certificateDTO);
        assertEquals(certificateDTO, actual);
    }

    @Test
    void testAddNegative() throws ServiceException {
        lenient().when(dao.create(any(GiftCertificate.class))).thenReturn(certificate);
        CertificateDTO actual = service.add(certificateDTO);
        assertNotEquals(null, actual);
    }

    @Test
    void testAddServiceException() {
        lenient().when(dao.create(any(GiftCertificate.class))).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(ServiceException.class, () -> service.add(certificateDTO));
    }

    @Test
    void testUpdateServiceException() {
        lenient().when(dao.update(any(GiftCertificate.class))).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(ServiceException.class, () -> service.update(certificateDTO));
    }

    @Test
    void testFilterByParametersPositive() throws ServiceException, IncorrectArgumentException {
        lenient().when(dao.getCountOfEntities()).thenReturn(10L);
        lenient().when(dao.filterByParameters(any(), anyInt(), anyInt()))
                .thenReturn(certificates);
        List<CertificateDTO> actual = service
                .filterByParameters(new FilterParams(), 2, 3);
        List<CertificateDTO> expected = new ArrayList<>();
        expected.add(certificateDTO);
        assertEquals(expected, actual);
    }

    @Test
    void testFilterByParametersNegative() throws ServiceException, IncorrectArgumentException {
        lenient().when(dao.getCountOfEntities()).thenReturn(10L);
        lenient().when(dao.filterByParameters(any(), anyInt(), anyInt()))
                .thenReturn(certificates);
        List<CertificateDTO> actual = service
                .filterByParameters(new FilterParams(), 2, 3);
        assertNotEquals(0, actual.size());
    }

    @Test
    void testFilterByParametersServiceException() {
        lenient().when(dao.getCountOfEntities()).thenReturn(10L);
        lenient().when(dao.filterByParameters(any(), anyInt(), anyInt()))
                .thenThrow(EmptyResultDataAccessException.class);
        assertThrows(ServiceException.class, () -> service
                .filterByParameters(new FilterParams(), 2, 3));
    }

    @Test
    void testFilterByParametersIncorrectArgumentException() {
        lenient().when(dao.getCountOfEntities()).thenReturn(9L);
        lenient().when(dao.filterByParameters(any(), anyInt(), anyInt()))
                .thenReturn(certificates);
        assertThrows(IncorrectArgumentException.class, () -> service
                .filterByParameters(new FilterParams(), 4, 3));
    }

    @Test
    void testCountPositive() throws ServiceException {
        lenient().when(dao.getCountOfEntities()).thenReturn(10L);
        long actual = service.count();
        assertEquals(10L, actual);
    }

    @Test
    void testCountNegative() throws ServiceException {
        lenient().when(dao.getCountOfEntities()).thenReturn(10L);
        long actual = service.count();
        assertNotEquals(1L, actual);
    }

    @Test
    void testCountServiceException() {
        lenient().when(dao.getCountOfEntities()).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(ServiceException.class, () -> service.count());
    }
}
