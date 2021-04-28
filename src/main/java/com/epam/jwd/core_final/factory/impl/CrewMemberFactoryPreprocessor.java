package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.InvalidArgumentException;
import com.epam.jwd.core_final.factory.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrewMemberFactoryPreprocessor implements EntityFactory<CrewMember> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrewMemberFactoryPreprocessor.class);
    private final EntityFactory<CrewMember> factory;

    CrewMemberFactoryPreprocessor(EntityFactory<CrewMember> factory) {
        this.factory = factory;
    }

    @Override
    public CrewMember create(Object... args) {
        try {
            if ((args.length != 3 && args.length != 5)
                    || !(args[0] instanceof String)
                    || !(args[1] instanceof Role)
                    || !(args[2] instanceof Rank)
                    || (args.length == 5
                    && !(args[3] instanceof Long)
                    && !(args[4] instanceof Boolean))) {
                throw new InvalidArgumentException(CANNOT_CREATE_OBJ_MSG + args[0]);
            }
            return factory.create(args);
        } catch (InvalidArgumentException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public CrewMember assignId(Long id, CrewMember entity) {
        return factory.assignId(id, entity);
    }


}
