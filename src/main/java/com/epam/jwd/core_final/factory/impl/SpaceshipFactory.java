package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.EntityFactory;

import java.util.Map;

public final class SpaceshipFactory implements EntityFactory<Spaceship> {
    private static EntityFactory<Spaceship> factory;

    private SpaceshipFactory() {
    }

    public static EntityFactory<Spaceship> getInstance() {
        if (factory == null) {
            factory = new SpaceshipFactoryPreprocessor(new SpaceshipFactory());
        }
        return factory;
    }

    @Override
    public Spaceship create(Object... args) {
        String name = (String) args[0];
        Map<Role, Short> crew = (Map<Role, Short>) args[1];
        Long flightDistance = (Long) args[2];
        if (args.length == 3) {
            return new Spaceship(name, crew, flightDistance);
        } else {
            Long id = (Long) args[3];
            Boolean isReady = (Boolean) args[4];
            return new Spaceship(id, name, crew, flightDistance, isReady);
        }
    }

    @Override
    public Spaceship assignId(Long id, Spaceship entity) {
        return new Spaceship(id, entity.getName(), entity.getCrew(), entity.getFlightDistance(),
                entity.isReadyForNextMissions());
    }
}
