package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Spaceship;

import java.time.LocalDateTime;
import java.util.List;

public class FlightMissionCriteriaBuilder extends CriteriaBuilder<FlightMission> {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long distance;
    private Spaceship assignedSpaceship;
    private List<CrewMember> assignedCrew;
    private MissionResult missionResult;
    private Planet from;
    private Planet to;

    public FlightMissionCriteriaBuilder setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public FlightMissionCriteriaBuilder setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public FlightMissionCriteriaBuilder setDistance(Long distance) {
        this.distance = distance;
        return this;
    }

    public FlightMissionCriteriaBuilder setAssignedSpaceship(Spaceship assignedSpaceship) {
        this.assignedSpaceship = assignedSpaceship;
        return this;
    }

    public FlightMissionCriteriaBuilder setAssignedCrew(List<CrewMember> assignedCrew) {
        this.assignedCrew = assignedCrew;
        return this;
    }

    public FlightMissionCriteriaBuilder setMissionResult(MissionResult missionResult) {
        this.missionResult = missionResult;
        return this;
    }

    public FlightMissionCriteriaBuilder setFrom(Planet from) {
        this.from = from;
        return this;
    }

    public FlightMissionCriteriaBuilder setTo(Planet to) {
        this.to = to;
        return this;
    }

    @Override
    public FlightMissionCriteria getResult() {
        return new FlightMissionCriteria(id, name, startDate, endDate, distance, assignedSpaceship, assignedCrew,
                missionResult, from, to);
    }
}
