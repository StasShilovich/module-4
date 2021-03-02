package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.entity.Order;
import com.epam.esm.model.dao.entity.OrderCertificate;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.model.dao.DaoConstant.CERTIFICATES;
import static com.epam.esm.model.dao.DaoConstant.ID;
import static com.epam.esm.model.dao.DaoConstant.NAME;
import static com.epam.esm.model.dao.DaoConstant.TAGS;

@Repository
public class OrderDaoImpl extends GenericDaoImpl<Order> implements OrderDao {

    public OrderDaoImpl() {
        super(Order.class);
    }

    @Override
    public Order create(Order entity) {
        Order order = Order.builder()
                .userId(entity.getUserId())
                .purchaseTime(entity.getPurchaseTime())
                .certificates(new HashSet<>())
                .build();
        entityManager.persist(order);
        Set<OrderCertificate> certificates = entity.getCertificates();
        if (!certificates.isEmpty()) {
            Order orderId = Order.builder().id(order.getId()).build();
            certificates.forEach(o -> {
                o.setOrder(orderId);
                entityManager.persist(o);
            });
        }
        Optional<Order> result = findById(order.getId());
        result.ifPresent(o -> o.setCertificates(certificates));
        return result.orElseGet(Order::new);
    }

    @Override
    public Tag getTopUserTag() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> query = builder.createQuery();
        /*
          SELECT t.id ,t.name,sum(oc.quantity) FROM  certificates.order o
          join certificates.order_certificate oc on o.id=oc.id_order
          join certificates.gift_certificate gc on oc.id_certificate=gc.id
          join certificates.tag_certificate tc on gc.id=tc.id_certificate
          join tag t on tc.id_tag=t.id
          where o.user_id=
          (SELECT u.id  FROM certificates.user u
          join  certificates.order o on u.id=o.user_id
          join certificates.order_certificate oc on o.id=oc.id_order
          group by u.id
          order by sum(oc.quantity*oc.one_cost) desc limit 1)
          group by t.name limit 1
         */
        Root<User> root = query.from(User.class);
        Join<Object, Object> join = root.join("orders").join(CERTIFICATES);
        Expression<Number> prod = builder.prod(join.get("quantity"), join.get("oneCost"));
        query.select(root.get(ID))
                .groupBy(root.get(ID))
                .orderBy(builder.desc(builder.sum(prod)));
        long userId = (long) entityManager.createQuery(query).setMaxResults(1).getSingleResult();

        CriteriaQuery<Tuple> orderQuery = builder.createTupleQuery();
        Root<Order> orderRoot = orderQuery.from(Order.class);
        Join<Object, Object> tagJoin = orderRoot.join(CERTIFICATES)
                .join("certificate").join(TAGS);
        orderQuery.select(builder.tuple(tagJoin.get(ID), tagJoin.get(NAME)))
                .where(builder.equal(orderRoot.get("userId"), userId))
                .groupBy(tagJoin.get(NAME));
        Optional<Tuple> tuple = entityManager.createQuery(orderQuery)
                .setMaxResults(1)
                .getResultList().stream().findFirst();
        return !tuple.isPresent() ? new Tag() : Tag.builder()
                .id((long) tuple.get().get(0))
                .name(tuple.get().get(1).toString()).build();
    }
}
