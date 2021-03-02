package com.epam.esm.model.dao.entity;

import com.epam.esm.model.dao.AuditListener;
import com.epam.esm.model.dao.GenericEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditListener.class)
@Entity(name = "tag")
public class Tag extends GenericEntity implements Comparable<Tag> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;

    @Override
    public int compareTo(Tag tag) {
        return name.compareTo(tag.getName());
    }
}
