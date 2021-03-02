package com.epam.esm.model.dao.entity;

import com.epam.esm.model.dao.AuditListener;
import com.epam.esm.model.dao.GenericEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditListener.class)
@Entity(name = "order_certificate")
public class OrderCertificate extends GenericEntity implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_order")
    @EqualsAndHashCode.Exclude
    private Order order;
    @Id
    @ManyToOne
    @JoinColumn(name = "id_certificate")
    @EqualsAndHashCode.Exclude
    private GiftCertificate certificate;
    @Column(name = "quantity")
    private Long quantity;
    @Column(name = "one_cost")
    private BigDecimal oneCost;

    @Override
    public Long getId() {
        return order.getId() + certificate.getId();
    }
}
