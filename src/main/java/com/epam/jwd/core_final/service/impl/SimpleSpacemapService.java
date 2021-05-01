package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.service.SpacemapService;

import java.util.Collection;

public enum SimpleSpacemapService implements SpacemapService {
    INSTANCE;

    @Override
    public Planet getRandomPlanet() {
        Collection<Planet> planets = Application.context.retrieveBaseEntityList(Planet.class);
        return planets.stream().findAny().orElse(null);
    }

    @Override
    public double getDistanceBetweenPlanets(Planet first, Planet second) {
        return Math.sqrt(Math.pow(second.getLocation().getX() - first.getLocation().getX(), 2)
                + Math.pow(second.getLocation().getY() - first.getLocation().getY(), 2));
    }
}
