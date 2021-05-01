package com.epam.jwd.core_final.domain;

import com.epam.jwd.core_final.service.impl.SimpleCrewService;
import com.epam.jwd.core_final.service.impl.SimpleSpacemapService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * Expected fields:
 * <p>
 * missions name {@link String}
 * start date {@link java.time.LocalDate}
 * end date {@link java.time.LocalDate}
 * distance {@link Long} - missions distance
 * assignedSpaceShift {@link Spaceship} - not defined by default
 * assignedCrew {@link java.util.List<CrewMember>} - list of missions members based on ship capacity - not defined by default
 * missionResult {@link MissionResult}
 * from {@link Planet}
 * to {@link Planet}
 */
public class FlightMission extends AbstractBaseEntity {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Long distance;
    private final Spaceship assignedSpaceship;
    private final List<CrewMember> assignedCrew;
    private final MissionResult missionResult;
    private final Planet from;
    private final Planet to;

    public FlightMission(String name, LocalDateTime startDate, Spaceship assignedSpaceship,
                         List<CrewMember> assignedCrew, MissionResult missionResult, Planet from, Planet to) {
        super(name);
        this.startDate = startDate;
        this.distance = Math.round(SimpleSpacemapService.INSTANCE.getDistanceBetweenPlanets(from, to));
        this.endDate = startDate.plusSeconds(this.distance);
        this.assignedSpaceship = assignedSpaceship;
        this.assignedCrew = assignedCrew;
        this.missionResult = missionResult;
        this.from = from;
        this.to = to;
    }

    public FlightMission(Long id, String name, LocalDateTime startDate, Spaceship assignedSpaceship,
                         List<CrewMember> assignedCrew, MissionResult missionResult, Planet from, Planet to) {
        super(id, name);
        this.startDate = startDate;
        this.distance = Math.round(SimpleSpacemapService.INSTANCE.getDistanceBetweenPlanets(from, to));
//        this.distance = 60L; //todo for tests
        this.endDate = startDate.plusSeconds(this.distance);
        this.assignedSpaceship = assignedSpaceship;
        this.assignedCrew = assignedCrew;
        this.missionResult = missionResult;
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Long getDistance() {
        return distance;
    }

    public Spaceship getAssignedSpaceship() {
        return assignedSpaceship;
    }

    public List<CrewMember> getAssignedCrew() {
        return assignedCrew;
    }

    public MissionResult getMissionResult() {
        return missionResult;
    }

    public Planet getFrom() {
        return from;
    }

    public Planet getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FlightMission mission = (FlightMission) o;

        if (!Objects.equals(startDate, mission.startDate)) return false;
        if (!Objects.equals(endDate, mission.endDate)) return false;
        if (!Objects.equals(distance, mission.distance)) return false;
        if (!Objects.equals(assignedSpaceship, mission.assignedSpaceship)) return false;
        if (!Objects.equals(assignedCrew, mission.assignedCrew)) return false;
        if (missionResult != mission.missionResult) return false;
        if (!Objects.equals(from, mission.from)) return false;
        return Objects.equals(to, mission.to);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (distance != null ? distance.hashCode() : 0);
        result = 31 * result + (assignedSpaceship != null ? assignedSpaceship.hashCode() : 0);
        result = 31 * result + (assignedCrew != null ? assignedCrew.hashCode() : 0);
        result = 31 * result + (missionResult != null ? missionResult.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        ApplicationProperties properties = ApplicationProperties.getInstance();
        int dateLength = properties.dateTimeFormat.length();
        return String.format("#%-3d %-15s %" + dateLength + "s - %" + dateLength
                        + "s from %-15s to %-15s on %-15s (dist: %-5d, team: %2d/%-2d) -> %s",
                id,
                name,
                startDate.format(DateTimeFormatter.ofPattern(properties.dateTimeFormat)),
                endDate.format(DateTimeFormatter.ofPattern(properties.dateTimeFormat)),
                from.getName(),
                to.getName(),
                (assignedSpaceship == null) ? "[REMOVED]" : assignedSpaceship.getName(),
                distance,
                assignedCrew.size(),
                (assignedSpaceship == null) ? 0 : SimpleCrewService.INSTANCE.getMembersNeeded(assignedSpaceship),
                missionResult
        );
    }
}
