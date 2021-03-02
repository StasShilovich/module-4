package com.epam.esm.model.dao.impl;

import com.epam.esm.model.common.FilterParams;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.entity.GiftCertificate;
import com.epam.esm.model.common.SortType;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.GiftCertificateHandler;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

import static com.epam.esm.model.dao.DaoConstant.CREATE_DATE;
import static com.epam.esm.model.dao.DaoConstant.DATE;
import static com.epam.esm.model.dao.DaoConstant.DESCRIPTION;
import static com.epam.esm.model.dao.DaoConstant.NAME;
import static com.epam.esm.model.dao.DaoConstant.PERCENT;
import static com.epam.esm.model.dao.DaoConstant.TAGS;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Repository
public class GiftCertificateDaoImpl extends GenericDaoImpl<GiftCertificate> implements GiftCertificateDao {

    public GiftCertificateDaoImpl() {
        super(GiftCertificate.class);
    }

    @Override
    public List<GiftCertificate> filterByParameters(FilterParams filterParams, int offset, int limit) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        buildFilterQuery(builder, root, query, filterParams);
        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        Long id = giftCertificate.getId();
        GiftCertificate certificateDB = findById(id).get();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        Map<String, Object> changedParameters = GiftCertificateHandler.defineChanges(giftCertificate, certificateDB);
        if (!changedParameters.isEmpty()) {
            CriteriaUpdate<GiftCertificate> update = builder.createCriteriaUpdate(GiftCertificate.class);
            update.from(GiftCertificate.class);
            changedParameters.forEach(update::set);
            entityManager.createQuery(update).executeUpdate();
        }
        List<Tag> tagsDB = certificateDB.getTags();
        List<Tag> tagsNew = giftCertificate.getTags();
        if (tagsNew != null && GiftCertificateHandler.isNotEqualsList(tagsNew, tagsDB)) {
            certificateDB.setTags(tagsNew);
            entityManager.merge(certificateDB);
        }
        return certificateDB;
    }

    private void buildFilterQuery(
            CriteriaBuilder builder, Root<GiftCertificate> root, CriteriaQuery<GiftCertificate> query,
            FilterParams filterParams) {
        query.select(root);
        Predicate predicatePart = builder.and();
        if (isNotEmpty(filterParams.getPart())) {
            predicatePart = predicatePart(builder, root, filterParams.getPart());
        }
        Predicate predicateTag = builder.and();
        if (filterParams.getTags() != null && !filterParams.getTags().isEmpty()) {
            predicateTag = predicateTag(builder, root, filterParams.getTags());
        }
        query.where(builder.and(predicatePart, predicateTag));

        Path<Object> path = isNotEmpty(filterParams.getSortBy()) && filterParams.getSortBy().equalsIgnoreCase(DATE) ?
                root.get(CREATE_DATE) :
                root.get(NAME);
        query.orderBy(filterParams.getType() == SortType.DESC ? builder.desc(path) : builder.asc(path));
    }

    private Predicate predicatePart(CriteriaBuilder builder, Root<GiftCertificate> root, String part) {
        String partLike = PERCENT + part + PERCENT;
        Predicate likeName = builder.like(root.get(NAME), partLike);
        Predicate likeDescription = builder.like(root.get(DESCRIPTION), partLike);
        return builder.or(likeName, likeDescription);
    }

    private Predicate predicateTag(CriteriaBuilder builder, Root<GiftCertificate> root, List<String> tags) {
        Predicate predicate;
        if (tags.size() == 1) {
            predicate = builder.equal(root.join(TAGS).get(NAME), tags.get(0));
        } else {
            Predicate tagOne = builder.equal(root.join(TAGS).get(NAME), tags.get(0));
            Predicate tagTwo = builder.equal(root.join(TAGS).get(NAME), tags.get(1));
            predicate = builder.and(tagOne, tagTwo);
        }
        return predicate;
    }

}
