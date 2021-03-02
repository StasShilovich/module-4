package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.Tag;
import org.springframework.stereotype.Repository;

@Repository
public class TagDaoImpl extends GenericDaoImpl<Tag> implements TagDao {

    public TagDaoImpl() {
        super(Tag.class);
    }
}
