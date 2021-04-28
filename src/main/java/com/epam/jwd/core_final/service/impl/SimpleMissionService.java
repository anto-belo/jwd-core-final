package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.MissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.jwd.core_final.context.Application.context;

public enum SimpleMissionService implements MissionService {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMissionService.class);

    @Override
    public List<FlightMission> findAllMissions() {
        return new ArrayList<>(context.retrieveBaseEntityList(FlightMission.class));
    }

    @Override
    public List<FlightMission> findAllMissionsByCriteria(Criteria<? extends FlightMission> criteria) {
        return context.retrieveBaseEntityList(FlightMission.class).stream()
                .filter(((FlightMissionCriteria) criteria)::isSuitsToCriteria)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FlightMission> findMissionByCriteria(Criteria<? extends FlightMission> criteria) {
        return context.retrieveBaseEntityList(FlightMission.class).stream()
                .filter(((FlightMissionCriteria) criteria)::isSuitsToCriteria)
                .findFirst();
    }

    @Override
    public FlightMission updateMissionDetails(FlightMission newFlightMission) {
        Long missionToUpdateId = newFlightMission.getId();
        Optional<FlightMission> oMission = getFlightMissionById(missionToUpdateId);
        String missionNotExistMsg = "Unable to update mission object, because it does not exist: ";
        FlightMission currentFlightMission = oMission.orElseThrow(()
                -> new UnknownEntityException(missionNotExistMsg + newFlightMission.getName()));
        Collection<FlightMission> missions = context.retrieveBaseEntityList(FlightMission.class);
        ((List<FlightMission>) missions).set(((List<FlightMission>) missions)
                .indexOf(currentFlightMission), newFlightMission);
        return getFlightMissionById(missionToUpdateId).orElse(null);
    }

    public void changeMissionStatusEnded(FlightMission mission) {
        EntityFactory<FlightMission> factory = FlightMissionFactory.getInstance();
        MissionResult result = (Math.random() * 100 < 50) ? MissionResult.COMPLETED : MissionResult.FAILED;
        if (result == MissionResult.FAILED) {
            setMissionMembersNotReady(mission);
        }
        FlightMission updatedMission = factory.create(mission.getName(), mission.getStartDate(),
                mission.getAssignedSpaceship(), mission.getAssignedCrew(), result,
                mission.getFrom(), mission.getTo(), mission.getId());
        updateMissionDetails(updatedMission);
    }

    private void setMissionMembersNotReady(FlightMission mission) {
        EntityFactory<CrewMember> crewFactory = CrewMemberFactory.getInstance();
        SimpleCrewService crewService = SimpleCrewService.INSTANCE;
        for (CrewMember member : mission.getAssignedCrew()) {
            CrewMember updatedMember = crewFactory.create(member.getName(), member.getRole(), member.getRank(),
                    false, member.getId());
            crewService.updateCrewMemberDetails(updatedMember);
        }

        Spaceship spaceship = mission.getAssignedSpaceship();
        EntityFactory<Spaceship> spaceshipFactory = SpaceshipFactory.getInstance();
        Spaceship updatedSpaceship = spaceshipFactory.create(spaceship.getName(), spaceship.getCrew(),
                spaceship.getFlightDistance(), spaceship.getId(), false);
        SimpleSpaceshipService.INSTANCE.updateSpaceshipDetails(updatedSpaceship);
    }

    public void changeMissionStatusInProgress(FlightMission mission) {
        EntityFactory<FlightMission> factory = FlightMissionFactory.getInstance();
        FlightMission updatedMission = factory.create(mission.getName(), mission.getStartDate(),
                mission.getAssignedSpaceship(), mission.getAssignedCrew(), MissionResult.IN_PROGRESS,
                mission.getFrom(), mission.getTo(), mission.getId());
        updateMissionDetails(updatedMission);
    }

    public void mayChangeMissionStatusCancelled(FlightMission mission) {
        EntityFactory<FlightMission> factory = FlightMissionFactory.getInstance();
        MissionResult result = (Math.random() * 100 < 20) ? MissionResult.CANCELLED : MissionResult.IN_PROGRESS;
        if (result == MissionResult.CANCELLED) {
            FlightMission updatedMission = factory.create(mission.getName(), mission.getStartDate(),
                    null, null, result, mission.getFrom(), mission.getTo(), mission.getId());
            updateMissionDetails(updatedMission);
        }
    }

    public Optional<FlightMission> getFlightMissionById(Long id) {
        return context.retrieveBaseEntityList(FlightMission.class).stream()
                .filter(s -> id.equals(s.getId()))
                .findFirst();
    }

    public boolean isIntersectingMissions(FlightMission m1, FlightMission m2) {
        LocalDateTime m1Start = m1.getStartDate();
        LocalDateTime m1End = m1.getEndDate();
        LocalDateTime m2Start = m2.getStartDate();
        LocalDateTime m2End = m2.getEndDate();
        return !(m1Start.isBefore(m2Start) && m2Start.isBefore(m1End))
                || (m1Start.isBefore(m2End) && m2End.isBefore(m1End))
                || (m2Start.isBefore(m1Start) && m1Start.isBefore(m2End))
                || (m2Start.isBefore(m1End) && m1End.isBefore(m2End));
    }

    @Override
    public FlightMission createMission(FlightMission flightMission) {
        if (!isUniqueName(flightMission.getName())) {
            String missionExistsMsg = "Mission already exists";
            throw new EntityDuplicateException(missionExistsMsg);
        }
        try {
            return context.addEntity(flightMission);
        } catch (InvalidStateException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return flightMission;
    }

    private boolean isUniqueName(String name) {
        return context.retrieveBaseEntityList(FlightMission.class).stream()
                .noneMatch(s -> s.getName().equals(name));
    }
}
