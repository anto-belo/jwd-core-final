package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;

import java.util.Map;

public class SpaceshipCriteriaBuilder extends CriteriaBuilder<Spaceship> {
    private Map<Role, Short> crew;
    private Long flightDistance;
    private Boolean isReadyForNextMissions;

    public SpaceshipCriteriaBuilder setCrew(Map<Role, Short> crew) {
        this.crew = crew;
        return this;
    }

    public SpaceshipCriteriaBuilder setFlightDistance(Long flightDistance) {
        this.flightDistance = flightDistance;
        return this;
    }

    public SpaceshipCriteriaBuilder setReadyForNextMissions(Boolean readyForNextMissions) {
        isReadyForNextMissions = readyForNextMissions;
        return this;
    }

    @Override
    public SpaceshipCriteria getResult() {
        return new SpaceshipCriteria(id, name, crew, flightDistance, isReadyForNextMissions);
    }
}
