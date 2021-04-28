package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.AbstractBaseEntity;
import com.epam.jwd.core_final.domain.BaseEntity;

/**
 * Should be a builder for {@link BaseEntity} fields
 */
public abstract class Criteria<T extends BaseEntity> {
    protected final Long id;
    protected final String name;

    protected Criteria(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isSuitsToCriteria(AbstractBaseEntity entity) {
        return (id == null || id.equals(entity.getId()))
                && (name == null || name.equals(entity.getName()));
    }

    static abstract class CriteriaBuilder<T extends BaseEntity> {
        protected Long id;
        protected String name;

        public CriteriaBuilder<T> setId(Long id) {
            this.id = id;
            return this;
        }

        public CriteriaBuilder<T> setName(String name) {
            this.name = name;
            return this;
        }

        public abstract Criteria<T> getResult();
    }
}
