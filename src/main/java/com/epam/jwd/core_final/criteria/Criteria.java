package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.AbstractBaseEntity;
import com.epam.jwd.core_final.domain.BaseEntity;

/**
 * Should be a builder for {@link BaseEntity} fields
 */
public abstract class Criteria<T extends BaseEntity> {
    final Long id;
    final String name;

    Criteria(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isSuitsToCriteria(AbstractBaseEntity entity) {
        return (id == null || id.equals(entity.getId()))
                && (name == null || name.equals(entity.getName()));
    }

}
