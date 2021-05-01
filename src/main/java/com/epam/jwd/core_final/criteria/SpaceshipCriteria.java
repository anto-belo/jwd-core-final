package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.AbstractBaseEntity;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;

import java.util.Map;

/**
 * Should be a builder for {@link Spaceship} fields
 */
public class SpaceshipCriteria extends Criteria<Spaceship> {
    private final Map<Role, Short> crew;
    private final Long flightDistance;
    private final Boolean isReadyForNextMissions;

    SpaceshipCriteria(Long id, String name, Map<Role, Short> crew, Long flightDistance,
                      Boolean isReadyForNextMissions) {
        super(id, name);
        this.crew = crew;
        this.flightDistance = flightDistance;
        this.isReadyForNextMissions = isReadyForNextMissions;
    }

    @Override
    public boolean isSuitsToCriteria(AbstractBaseEntity entity) {
        Spaceship s = (Spaceship) entity;
        return super.isSuitsToCriteria(entity)
                && (crew == null || crew.equals(s.getCrew()))
                && (flightDistance == null || flightDistance.equals(s.getFlightDistance()))
                && (isReadyForNextMissions == null || isReadyForNextMissions.equals(s.isReadyForNextMissions()));
    }

}
