package com.epam.jwd.core_final.factory;

import com.epam.jwd.core_final.domain.BaseEntity;

public interface EntityFactory<T extends BaseEntity> {
    String CANNOT_CREATE_OBJ_MSG = "Object can't be created with such arguments: ";

    T create(Object... args);

    T assignId(Long id, T entity);
}
