package com.epam.jwd.core_final.util;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.exception.*;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.SpaceshipService;
import com.epam.jwd.core_final.service.impl.SimpleCrewService;
import com.epam.jwd.core_final.service.impl.SimpleMissionService;
import com.epam.jwd.core_final.service.impl.SimpleSpaceshipService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public enum UserImitator {
    INSTANCE;

    public FlightMission generateMission(String name) throws EntityDuplicateException, NoVacantEntitiesException {
        FlightMission mission = getMissionTemplate(name);
        SimpleMissionService service = SimpleMissionService.INSTANCE;
        mission = service.createMission(mission);
        assignSpaceshipOnMission(mission);
        assignCrewOnMission(mission);
        return service.getFlightMissionById(mission.getId()).orElse(mission);
    }

    private FlightMission getMissionTemplate(String name) {
        LocalDateTime startDate = LocalDateTime.now().plusHours((long) (Math.random() * 12))
                .plusMinutes((long) (Math.random() * 60));
        List<Planet> planets = new ArrayList<>(Application.context.retrieveBaseEntityList(Planet.class));
        Planet from = planets.get((int) (Math.random() * (planets.size() - 1)));
        Planet to;
        do {
            to = planets.get((int) (Math.random() * (planets.size() - 1)));
        } while (Objects.equals(from, to));
        return FlightMissionFactory.getInstance().create(name, startDate, null, null, MissionResult.PLANNED, from, to);
    }

    private void assignSpaceshipOnMission(FlightMission mission) throws NoVacantEntitiesException {
        SpaceshipService service = SimpleSpaceshipService.INSTANCE;
        List<Spaceship> spaceships = service.findAllSpaceships();
        int i = 0;
        while (mission.getAssignedSpaceship() == null && i < spaceships.size()) {
            try {
                service.assignSpaceshipOnMission(spaceships.get(i++));
                mission = SimpleMissionService.INSTANCE.getFlightMissionById(mission.getId()).orElse(mission);
            } catch (NotReadyForNextMissions | NoSuitableMissionsException ignored) {
            }
        }
        if (mission.getAssignedSpaceship() == null) {
            String noVacantShipsMsg = "There's no vacant spaceships at the moment";
            throw new NoVacantEntitiesException(noVacantShipsMsg);
        }
    }

    private void assignCrewOnMission(FlightMission mission) throws NoVacantEntitiesException {
        mission = SimpleMissionService.INSTANCE.getFlightMissionById(mission.getId()).orElse(mission);
        Spaceship spaceship = mission.getAssignedSpaceship();
        if (spaceship == null) {
            throw new SpaceshipNotAssignedException();
        }

        SimpleCrewService service = SimpleCrewService.INSTANCE;
        List<CrewMember> members = service.findAllCrewMembers();
        int i = 0;
        int membersNeeded = service.getMembersNeeded(spaceship);
        while (mission.getAssignedCrew().size() != membersNeeded && i < members.size()) {
            try {
                service.assignCrewMemberOnMission(members.get(i++));
                mission = SimpleMissionService.INSTANCE.getFlightMissionById(mission.getId()).orElse(mission);
            } catch (NotReadyForNextMissions | NoSuitableMissionsException ignored) {
            }
        }
        if (mission.getAssignedCrew().size() != membersNeeded) {
            String noVacantMembersMsg = "There's no vacant members at the moment";
            throw new NoVacantEntitiesException(noVacantMembersMsg);
        }
    }

    public FlightMission updateMission(FlightMission mission) {
        EntityFactory<FlightMission> factory = FlightMissionFactory.getInstance();
        List<Planet> planets = new ArrayList<>(Application.context.retrieveBaseEntityList(Planet.class));
        FlightMission updatedMission;
        switch ((int) (Math.random() * 2 + 1)) {
            case 1:
                LocalDateTime startDate = LocalDateTime.now().plusHours((long) (Math.random() * 12))
                        .plusMinutes((long) (Math.random() * 60));
                updatedMission = factory.create(mission.getName(), startDate, mission.getAssignedSpaceship(),
                        mission.getAssignedCrew(), mission.getMissionResult(), mission.getFrom(), mission.getTo(),
                        mission.getId());
                break;
            case 2:
                Planet from;
                do {
                    from = planets.get((int) (Math.random() * (planets.size() - 1)));
                } while (Objects.equals(from, mission.getTo()));
                updatedMission = factory.create(mission.getName(), mission.getStartDate(),
                        mission.getAssignedSpaceship(), mission.getAssignedCrew(), mission.getMissionResult(),
                        from, mission.getTo(), mission.getId());
                break;
            case 3:
                Planet to;
                do {
                    to = planets.get((int) (Math.random() * (planets.size() - 1)));
                } while (Objects.equals(mission.getFrom(), to));
                updatedMission = factory.create(mission.getName(), mission.getStartDate(),
                        mission.getAssignedSpaceship(), mission.getAssignedCrew(), mission.getMissionResult(),
                        mission.getFrom(), to, mission.getId());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + (int) (Math.random() * 2 + 1));
        }
        return SimpleMissionService.INSTANCE.updateMissionDetails(updatedMission);
    }
}
