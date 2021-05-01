package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.BaseEntity;

public abstract class CriteriaBuilder<T extends BaseEntity> {
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
