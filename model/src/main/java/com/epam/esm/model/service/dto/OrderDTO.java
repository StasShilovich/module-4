package com.epam.esm.model.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderDTO {

    private Long id;
    private String purchaseTime;
    private Long userId;
    private Set<OrderCertificateDTO> orderCertificates;
}
