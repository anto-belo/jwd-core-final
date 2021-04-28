package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.FlightMission} fields
 */
public class FlightMissionCriteria extends Criteria<FlightMission> {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Long distance;
    private final Spaceship assignedSpaceship;
    private final List<CrewMember> assignedCrew;
    private final MissionResult missionResult;
    private final Planet from;
    private final Planet to;

    private FlightMissionCriteria(Long id, String name, LocalDate startDate, LocalDate endDate, Long distance, Spaceship assignedSpaceship, List<CrewMember> assignedCrew, MissionResult missionResult, Planet from, Planet to) {
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

    static class FlightMissionCriteriaBuilder extends CriteriaBuilder<FlightMission> {
        private LocalDate startDate;
        private LocalDate endDate;
        private Long distance;
        private Spaceship assignedSpaceship;
        private List<CrewMember> assignedCrew;
        private MissionResult missionResult;
        private Planet from;
        private Planet to;

        public FlightMissionCriteriaBuilder setStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public FlightMissionCriteriaBuilder setEndDate(LocalDate endDate) {
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
        public Criteria<FlightMission> getResult() {
            return new FlightMissionCriteria(id, name, startDate, endDate, distance, assignedSpaceship, assignedCrew, missionResult, from, to);
        }
    }
}
