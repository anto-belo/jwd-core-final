package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Point;
import com.epam.jwd.core_final.factory.EntityFactory;

public final class PlanetFactory implements EntityFactory<Planet> {
    private static EntityFactory<Planet> factory;

    private PlanetFactory() {
    }

    public static EntityFactory<Planet> getInstance() {
        if (factory == null) {
            factory = new PlanetFactoryPreprocessor(new PlanetFactory());
        }
        return factory;
    }

    @Override
    public Planet create(Object... args) {
        String name = (String) args[0];
        Point location = (Point) args[1];
        return new Planet(name, location);
    }

    @Override
    public Planet assignId(Long id, Planet entity) {
        return new Planet(id, entity.getName(), entity.getLocation());
    }
}
