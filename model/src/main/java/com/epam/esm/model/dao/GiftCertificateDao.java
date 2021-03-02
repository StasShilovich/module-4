package com.epam.esm.model.dao;

import com.epam.esm.model.common.FilterParams;
import com.epam.esm.model.dao.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao extends GenericDao<GiftCertificate> {

    List<GiftCertificate> filterByParameters(FilterParams filterParams, int offset, int limit);

}
