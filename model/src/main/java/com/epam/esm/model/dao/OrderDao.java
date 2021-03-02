package com.epam.esm.model.dao;

import com.epam.esm.model.dao.entity.Order;
import com.epam.esm.model.dao.entity.Tag;

public interface OrderDao extends GenericDao<Order> {

    Tag getTopUserTag();
}
