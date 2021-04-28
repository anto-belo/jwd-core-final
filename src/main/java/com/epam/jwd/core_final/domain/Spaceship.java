package com.epam.jwd.core_final.domain;

import java.util.Map;
import java.util.Objects;

/**
 * crew {@link java.util.Map<Role, Short>}
 * flightDistance {@link Long} - total available flight distance
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class Spaceship extends AbstractBaseEntity {
    private final Map<Role, Short> crew;
    private final Long flightDistance;
    private final Boolean isReadyForNextMissions;

    public Spaceship(String name, Map<Role, Short> crew, Long flightDistance) {
        super(name);
        this.crew = crew;
        this.flightDistance = flightDistance;
        this.isReadyForNextMissions = true;
    }

    public Spaceship(Long id, String name, Map<Role, Short> crew, Long flightDistance, Boolean isReadyForNextMissions) {
        super(id, name);
        this.crew = crew;
        this.flightDistance = flightDistance;
        this.isReadyForNextMissions = isReadyForNextMissions;
    }

    public Map<Role, Short> getCrew() {
        return crew;
    }

    public Long getFlightDistance() {
        return flightDistance;
    }

    public Boolean isReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Spaceship spaceship = (Spaceship) o;

        if (!Objects.equals(crew, spaceship.crew)) return false;
        if (!Objects.equals(flightDistance, spaceship.flightDistance))
            return false;
        return Objects.equals(isReadyForNextMissions, spaceship.isReadyForNextMissions);
    }

    @Override
    public int hashCode() {
        int result = crew != null ? crew.hashCode() : 0;
        result = 31 * result + (flightDistance != null ? flightDistance.hashCode() : 0);
        result = 31 * result + (isReadyForNextMissions != null ? isReadyForNextMissions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Spaceship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", crew=" + crew +
                ", flightDistance=" + flightDistance +
                ", isReadyForNextMissions=" + isReadyForNextMissions +
                '}';
    }
}
