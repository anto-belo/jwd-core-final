package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.AbstractBaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Spaceship;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.FlightMission} fields
 */
public class FlightMissionCriteria extends Criteria<FlightMission> {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Long distance;
    private final Spaceship assignedSpaceship;
    private final List<CrewMember> assignedCrew;
    private final MissionResult missionResult;
    private final Planet from;
    private final Planet to;

    FlightMissionCriteria(Long id, String name, LocalDateTime startDate, LocalDateTime endDate, Long distance,
                          Spaceship assignedSpaceship, List<CrewMember> assignedCrew,
                          MissionResult missionResult, Planet from, Planet to) {
        super(id, name);
        this.startDate = startDate;
        this.endDate = endDate;
        this.distance = distance;
        this.assignedSpaceship = assignedSpaceship;
        this.assignedCrew = assignedCrew;
        this.missionResult = missionResult;
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean isSuitsToCriteria(AbstractBaseEntity entity) {
        FlightMission m = (FlightMission) entity;
        return super.isSuitsToCriteria(entity)
                && (startDate == null || startDate.equals(m.getStartDate()))
                && (endDate == null || endDate.equals(m.getEndDate()))
                && (distance == null || distance.equals(m.getDistance()))
                && (assignedSpaceship == null || assignedSpaceship.equals(m.getAssignedSpaceship()))
                && (assignedCrew == null || assignedCrew.equals(m.getAssignedCrew()))
                && (missionResult == null || missionResult.equals(m.getMissionResult()))
                && (from == null || from.equals(m.getFrom()))
                && (to == null || to.equals(m.getTo()));
    }

}
