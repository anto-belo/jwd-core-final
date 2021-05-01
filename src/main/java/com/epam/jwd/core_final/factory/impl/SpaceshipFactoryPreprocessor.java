package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.InvalidArgumentException;
import com.epam.jwd.core_final.factory.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SpaceshipFactoryPreprocessor implements EntityFactory<Spaceship> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceshipFactoryPreprocessor.class);
    private final EntityFactory<Spaceship> factory;

    SpaceshipFactoryPreprocessor(EntityFactory<Spaceship> factory) {
        this.factory = factory;
    }

    @Override
    public Spaceship create(Object... args) {
        try {
            if ((args.length != 3 && args.length != 5)
                    || !(args[0] instanceof String)
                    || !(args[1] instanceof Map)
                    || !(args[2] instanceof Long)
                    || (args.length == 5
                    && !(args[3] instanceof Boolean)
                    && !(args[4] instanceof Long))) {
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
    public Spaceship assignId(Long id, Spaceship entity) {
        return factory.assignId(id, entity);
    }
}
