package com.epam.jwd.core_final.service;

import com.epam.jwd.core_final.domain.Planet;

public interface SpacemapService {

    Planet getRandomPlanet();

    // Dijkstra ?
    double getDistanceBetweenPlanets(Planet first, Planet second);
}
