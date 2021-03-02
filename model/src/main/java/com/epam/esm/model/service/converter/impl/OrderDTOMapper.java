package com.epam.esm.model.service.converter.impl;

import com.epam.esm.model.dao.entity.Order;
import com.epam.esm.model.dao.entity.OrderCertificate;
import com.epam.esm.model.service.converter.DTOMapper;
import com.epam.esm.model.service.dto.OrderCertificateDTO;
import com.epam.esm.model.service.dto.OrderDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderDTOMapper implements DTOMapper<OrderDTO, Order> {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.s";
    private final OrderCertificateDTOMapper orderCertificateDTOMapper;

    public OrderDTOMapper(OrderCertificateDTOMapper orderCertificateDTOMapper) {
        this.orderCertificateDTOMapper = orderCertificateDTOMapper;
    }

    @Override
    public OrderDTO toDTO(Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        String purchaseTime = order.getPurchaseTime().format(formatter);
        Set<OrderCertificateDTO> set = order.getCertificates() != null ?
                order.getCertificates().stream()
                        .map(orderCertificateDTOMapper::toDTO).collect(Collectors.toSet()) : null;
        return OrderDTO.builder()
                .id(order.getId())
                .purchaseTime(purchaseTime)
                .userId(order.getUserId())
                .orderCertificates(set)
                .build();
    }

    @Override
    public Order fromDTO(OrderDTO orderDTO) {
        Set<OrderCertificate> set = orderDTO.getOrderCertificates() != null ? orderDTO.getOrderCertificates().stream()
                .map(orderCertificateDTOMapper::fromDTO).collect(Collectors.toSet()) : null;
        LocalDateTime purchaseTime = StringUtils.isNotEmpty(orderDTO.getPurchaseTime()) ?
                LocalDateTime.parse(orderDTO.getPurchaseTime()) : null;
        return Order.builder()
                .id(orderDTO.getId())
                .purchaseTime(purchaseTime)
                .userId(orderDTO.getUserId())
                .certificates(set)
                .build();
    }
}
