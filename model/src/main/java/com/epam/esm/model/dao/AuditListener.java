package com.epam.esm.model.dao;

import com.epam.esm.model.common.BeanUtil;
import com.epam.esm.model.dao.entity.Audit;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.time.LocalDateTime;

public class AuditListener {

    @PostPersist
    private void beforePersistOperation(Object object) {
        persistAudit("PERSIST", object);
    }

    @PostUpdate
    private void beforeUpdateOperation(Object object) {
        persistAudit("UPDATE", object);
    }

    @PostRemove
    private void beforeRemoveOperation(Object object) {
        persistAudit("REMOVE", object);
    }

    public void persistAudit(String operation, Object object) {
        GenericEntity entity = (GenericEntity) object;
        Audit audit = Audit.builder()
                .entityClass(entity.getClass().getSimpleName())
                .entityId(entity.getId())
                .operation(operation)
                .dateTime(LocalDateTime.now()).build();
        LocalContainerEntityManagerFactoryBean bean = BeanUtil.getBean(LocalContainerEntityManagerFactoryBean.class);
        EntityManager entityManager = bean.getObject().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(audit);
        entityManager.getTransaction().commit();
    }
}
