package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.exception.NoSuitableMissionsException;
import com.epam.jwd.core_final.exception.NotReadyForNextMissions;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.SpaceshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.jwd.core_final.context.Application.context;

public enum SimpleSpaceshipService implements SpaceshipService {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSpaceshipService.class);

    @Override
    public List<Spaceship> findAllSpaceships() {
        return new ArrayList<>(context.retrieveBaseEntityList(Spaceship.class));
    }

    @Override
    public List<Spaceship> findAllSpaceshipsByCriteria(Criteria<? extends Spaceship> criteria) {
        return context.retrieveBaseEntityList(Spaceship.class).stream()
                .filter(((SpaceshipCriteria) criteria)::isSuitsToCriteria)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria) {
        return context.retrieveBaseEntityList(Spaceship.class).stream()
                .filter(((SpaceshipCriteria) criteria)::isSuitsToCriteria)
                .findFirst();
    }

    @Override
    public Spaceship updateSpaceshipDetails(Spaceship newSpaceship) {
        Long spaceshipToUpdateId = newSpaceship.getId();
        Optional<Spaceship> oShip = getSpaceshipById(spaceshipToUpdateId);
        String spaceshipNotExistMsg = "Unable to update spaceship object, because it does not exist: ";
        Spaceship currentSpaceship = oShip.orElseThrow(()
                -> new UnknownEntityException(spaceshipNotExistMsg + newSpaceship.getName()));
        Collection<Spaceship> spaceships = context.retrieveBaseEntityList(Spaceship.class);
        ((List<Spaceship>) spaceships).set(((List<Spaceship>) spaceships).indexOf(currentSpaceship),
                newSpaceship);
        return getSpaceshipById(spaceshipToUpdateId).orElse(null);
    }

    public Optional<Spaceship> getSpaceshipById(Long id) {
        return context.retrieveBaseEntityList(Spaceship.class).stream()
                .filter(s -> id.equals(s.getId()))
                .findFirst();
    }

    @Override
    public void assignSpaceshipOnMission(Spaceship spaceship) throws RuntimeException {
        if (!spaceship.isReadyForNextMissions()) {
            String spaceshipNotReadyMsg = "Spaceship cannot be assigned on a mission, because it failed the last: ";
            throw new NotReadyForNextMissions(spaceshipNotReadyMsg + spaceship.getName());
        }

        Optional<FlightMission> oMission = getSuitableMission(spaceship);
        FlightMission mission = oMission.orElseThrow(NoSuitableMissionsException::new);

        EntityFactory<FlightMission> factory = FlightMissionFactory.getInstance();
        FlightMission updateMission = factory.create(mission.getName(), mission.getStartDate(),
                spaceship, mission.getAssignedCrew(), mission.getMissionResult(),
                mission.getFrom(), mission.getTo(), mission.getId());
        SimpleMissionService.INSTANCE.updateMissionDetails(updateMission);
    }

    private Optional<FlightMission> getSuitableMission(Spaceship spaceship) {
        return context.retrieveBaseEntityList(FlightMission.class).stream()
                .filter(s -> s.getAssignedSpaceship() == null)
                .filter(s -> spaceship.getFlightDistance() >= s.getDistance())
                .filter(s -> !isIntersectsWithSpaceshipMissions(s, spaceship))
                .findFirst();
    }

    private boolean isIntersectsWithSpaceshipMissions(FlightMission currentMission, Spaceship spaceship) {
        MissionService service = SimpleMissionService.INSTANCE;
        for (FlightMission mission : service.findAllMissions()) {
            if (!mission.equals(currentMission) && (mission.getMissionResult().equals(MissionResult.PLANNED)
                    || mission.getMissionResult().equals(MissionResult.IN_PROGRESS))
                    && mission.getAssignedSpaceship().equals(spaceship)
                    && service.isIntersectingMissions(currentMission, mission))
                return true;
        }
        return false;
    }

    @Override
    public Spaceship createSpaceship(Spaceship spaceship) throws RuntimeException {
        if (!isUniqueName(spaceship.getName())) {
            String spaceshipExistsMsg = "Spaceship already exists";
            throw new EntityDuplicateException(spaceshipExistsMsg);
        }
        try {
            return context.addEntity(spaceship);
        } catch (InvalidStateException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    private boolean isUniqueName(String name) {
        return context.retrieveBaseEntityList(Spaceship.class).stream()
                .noneMatch(s -> s.getName().equals(name));
    }

    @Override
    public boolean isAssignedOnAnyMissions(Spaceship spaceship) {
        List<FlightMission> missions = SimpleMissionService.INSTANCE.findAllMissions();
        if (missions.size() == 0) {
            return false;
        }
        return missions.stream()
                .filter(s -> s.getMissionResult() == MissionResult.PLANNED ||
                        s.getMissionResult() == MissionResult.IN_PROGRESS)
                .map(FlightMission::getAssignedSpaceship)
                .noneMatch(spaceship::equals);
    }
}
