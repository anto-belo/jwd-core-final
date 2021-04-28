package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Point;
import com.epam.jwd.core_final.exception.InvalidArgumentException;
import com.epam.jwd.core_final.factory.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanetFactoryPreprocessor implements EntityFactory<Planet> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanetFactoryPreprocessor.class);
    private final EntityFactory<Planet> factory;

    PlanetFactoryPreprocessor(EntityFactory<Planet> factory) {
        this.factory = factory;
    }

    @Override
    public Planet create(Object... args) {
        try {
            if (args.length != 2
                    || !(args[0] instanceof String)
                    || !(args[1] instanceof Point)) {
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
    public Planet assignId(Long id, Planet entity) {
        return factory.assignId(id, entity);
    }
}
