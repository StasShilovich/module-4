package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.entity.Order;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.service.OrderService;
import com.epam.esm.model.service.converter.impl.OrderDTOMapper;
import com.epam.esm.model.service.converter.impl.TagDTOMapper;
import com.epam.esm.model.service.dto.OrderDTO;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderDTOMapper orderMapper;
    private final TagDTOMapper tagMapper;

    public OrderServiceImpl(OrderDao orderDao, OrderDTOMapper orderMapper, TagDTOMapper tagMapper) {
        this.orderDao = orderDao;
        this.orderMapper = orderMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public OrderDTO add(OrderDTO orderDTO) throws ServiceException {
        try {
            Order order = orderMapper.fromDTO(orderDTO);
            Order orderDb = orderDao.create(order);
            return orderMapper.toDTO(orderDb);
        } catch (DataAccessException e) {
            log.error("Create order service exception", e);
            throw new ServiceException("Create order service exception", e);
        }
    }

    @Override
    public TagDTO getTopUserTag() throws ServiceException {
        try {
            Tag tag = orderDao.getTopUserTag();
            return tagMapper.toDTO(tag);
        } catch (DataAccessException e) {
            log.error("Top tag service exception", e);
            throw new ServiceException("Top tag service exception", e);
        }
    }
}
