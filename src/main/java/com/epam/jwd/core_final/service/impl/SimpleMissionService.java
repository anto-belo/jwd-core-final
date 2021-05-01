package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.MissionStatusChanger;
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
        ((List<FlightMission>) missions).set(((List<FlightMission>) missions).indexOf(currentFlightMission),
                newFlightMission);
        return getFlightMissionById(missionToUpdateId).orElse(null);
    }

    @Override
    public Optional<FlightMission> getFlightMissionById(Long id) {
        return context.retrieveBaseEntityList(FlightMission.class).stream()
                .filter(s -> id.equals(s.getId()))
                .findFirst();
    }

    @Override
    public boolean isIntersectingMissions(FlightMission m1, FlightMission m2) {
        LocalDateTime m1Start = m1.getStartDate();
        LocalDateTime m1End = m1.getEndDate();
        LocalDateTime m2Start = m2.getStartDate();
        LocalDateTime m2End = m2.getEndDate();
        return (m1Start.isBefore(m2Start) && m2Start.isBefore(m1End))
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

    public void updateMissionsStatus() {
        MissionStatusChanger changer = SimpleMissionStatusChanger.INSTANCE;
        for (FlightMission mission : Application.context.retrieveBaseEntityList(FlightMission.class)) {
            changer.update(mission);
        }
    }
}
