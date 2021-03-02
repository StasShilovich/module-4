package com.epam.esm.model.service.converter.impl;

import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.dao.entity.Order;
import com.epam.esm.model.dao.entity.OrderCertificate;
import com.epam.esm.model.service.converter.DTOMapper;
import com.epam.esm.model.service.dto.OrderCertificateDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderCertificateDTOMapper implements DTOMapper<OrderCertificateDTO, OrderCertificate> {
    @Override
    public OrderCertificateDTO toDTO(OrderCertificate orderCertificate) {
        return OrderCertificateDTO.builder()
                .orderId(orderCertificate.getOrder().getId())
                .certificateId(orderCertificate.getCertificate().getId())
                .oneCost(orderCertificate.getOneCost())
                .quantity(orderCertificate.getQuantity())
                .build();
    }

    @Override
    public OrderCertificate fromDTO(OrderCertificateDTO orderCertificateDTO) {
        return OrderCertificate.builder()
                .order(Order.builder()
                        .id(orderCertificateDTO.getOrderId())
                        .build())
                .certificate(GiftCertificate.builder()
                        .id(orderCertificateDTO.getCertificateId())
                        .build())
                .oneCost(orderCertificateDTO.getOneCost())
                .quantity(orderCertificateDTO.getQuantity())
                .build();
    }
}
