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

    private SpaceshipCriteria(Long id, String name, Map<Role, Short> crew, Long flightDistance, Boolean isReadyForNextMissions) {
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

    static class SpaceshipCriteriaBuilder extends CriteriaBuilder<Spaceship> {
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
        public Criteria<Spaceship> getResult() {
            return new SpaceshipCriteria(id, name, crew, flightDistance, isReadyForNextMissions);
        }
    }
}
