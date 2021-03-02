package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.entity.Order;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.service.OrderService;
import com.epam.esm.model.service.converter.impl.OrderCertificateDTOMapper;
import com.epam.esm.model.service.converter.impl.OrderDTOMapper;
import com.epam.esm.model.service.converter.impl.TagDTOMapper;
import com.epam.esm.model.service.dto.OrderDTO;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class OrderServiceImplTest {

    @Mock
    OrderDao dao;
    OrderService service;
    OrderDTOMapper orderMapper;
    TagDTOMapper tagDTOMapper;
    OrderCertificateDTOMapper orderCertificateDTOMapper;
    Order order;
    OrderDTO orderDTO;
    Tag tag;
    TagDTO tagDTO;

    @BeforeEach
    public void setUp() {
        tagDTOMapper = new TagDTOMapper();
        orderCertificateDTOMapper = new OrderCertificateDTOMapper();
        orderMapper = new OrderDTOMapper(orderCertificateDTOMapper);
        service = new OrderServiceImpl(dao, orderMapper, tagDTOMapper);
        order = Order.builder()
                .id(1L)
                .purchaseTime(LocalDateTime.parse("2021-01-12T18:15:00.0"))
                .userId(1L)
                .build();
        orderDTO = OrderDTO.builder()
                .id(1L)
                .purchaseTime("2021-01-12T18:15:00.0")
                .userId(1L)
                .build();
        tag = Tag.builder()
                .id(1L)
                .name("tag").build();
        tagDTO = TagDTO.builder()
                .id(1L)
                .name("tag").build();
    }

    @Test
    void testAddPositive() throws ServiceException {
        lenient().when(dao.create(any(Order.class))).thenReturn(order);
        OrderDTO actual = service.add(orderDTO);
        assertEquals(orderDTO, actual);
    }

    @Test
    void testAddNegative() throws ServiceException {
        lenient().when(dao.create(any(Order.class))).thenReturn(order);
        OrderDTO actual = service.add(orderDTO);
        assertNotEquals(null, actual);
    }

    @Test
    void testAddServiceException() {
        lenient().when(dao.create(any(Order.class))).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(ServiceException.class, () -> service.add(orderDTO));
    }

    @Test
    void testGetTopUserTagPositive() throws ServiceException {
        lenient().when(dao.getTopUserTag()).thenReturn(tag);
        TagDTO actual = service.getTopUserTag();
        assertEquals(tagDTO, actual);
    }

    @Test
    void testGetTopUserTagNegative() throws ServiceException {
        lenient().when(dao.getTopUserTag()).thenReturn(tag);
        TagDTO actual = service.getTopUserTag();
        assertNotEquals(null, actual);
    }

    @Test
    void testGetTopUserTagServiceException() {
        lenient().when(dao.getTopUserTag()).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(ServiceException.class, () -> service.getTopUserTag());
    }
}
