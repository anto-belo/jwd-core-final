package com.epam.jwd.core_final.util;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.NoSuitableMissionsException;
import com.epam.jwd.core_final.exception.NoVacantEntitiesException;
import com.epam.jwd.core_final.exception.NotAbleToUpdateException;
import com.epam.jwd.core_final.exception.NotReadyForNextMissions;
import com.epam.jwd.core_final.exception.SpaceshipNotAssignedException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.CrewService;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.SpaceshipService;
import com.epam.jwd.core_final.service.impl.SimpleCrewService;
import com.epam.jwd.core_final.service.impl.SimpleMissionService;
import com.epam.jwd.core_final.service.impl.SimpleSpaceshipService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public enum UserImitator {
    INSTANCE;

    public FlightMission generateMission(String name) throws EntityDuplicateException, NoVacantEntitiesException {
        FlightMission mission = getMissionTemplate(name);
        MissionService service = SimpleMissionService.INSTANCE;
        mission = service.createMission(mission);
        assignSpaceshipOnMission(mission);
        assignCrewOnMission(mission);
        return service.getFlightMissionById(mission.getId()).orElse(mission);
    }

    private FlightMission getMissionTemplate(String name) {
        LocalDateTime startDate = LocalDateTime.now().plusSeconds(45L);
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

        CrewService service = SimpleCrewService.INSTANCE;
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

    public Spaceship generateShip(String name) throws EntityDuplicateException {
        Map<Role, Short> crew = generateCrew();
        Long flightDistance = Math.round(Math.random() * 900_000 + 100_000);
        SpaceshipService service = SimpleSpaceshipService.INSTANCE;
        EntityFactory<Spaceship> factory = SpaceshipFactory.getInstance();
        return service.createSpaceship(factory.create(name, crew, flightDistance));
    }

    private Map<Role, Short> generateCrew() {
        Map<Role, Short> crew = new HashMap<>();
        for (Role r : Role.values()) {
            short count = (short) (Math.random() * 6 + 1);
            crew.put(r, count);
        }
        return crew;
    }

    public Spaceship updateSpaceship(Spaceship spaceship) throws NotAbleToUpdateException {
        SpaceshipService service = SimpleSpaceshipService.INSTANCE;
        if (service.isAssignedOnAnyMissions(spaceship)) {
            String shipIsNotVacantMsg = "Spaceship cannot be changed, because it's assigned at least on one mission";
            throw new NotAbleToUpdateException(shipIsNotVacantMsg);
        }
        Spaceship spaceshipUpdate;
        EntityFactory<Spaceship> factory = SpaceshipFactory.getInstance();
        if (Math.random() * 100 > 50) {
            Map<Role, Short> crew = generateCrew();
            spaceshipUpdate = factory.create(spaceship.getName(), crew, spaceship.getFlightDistance(),
                    spaceship.isReadyForNextMissions(), spaceship.getId());
        } else {
            Long flightDistance = Math.round(Math.random() * 900_000 + 100_000);
            spaceshipUpdate = factory.create(spaceship.getName(), spaceship.getCrew(), flightDistance,
                    spaceship.isReadyForNextMissions(), spaceship.getId());
        }
        return service.updateSpaceshipDetails(spaceshipUpdate);
    }

    public CrewMember generateMember(String name) throws EntityDuplicateException {
        Role role = Role.resolveRoleById((int) Math.floor(Math.random() * Role.values().length + 1));
        Rank rank = Rank.resolveRankById((int) Math.floor(Math.random() * Rank.values().length + 1));
        CrewService service = SimpleCrewService.INSTANCE;
        EntityFactory<CrewMember> factory = CrewMemberFactory.getInstance();
        return service.createCrewMember(factory.create(name, role, rank));
    }

    public CrewMember updateCrewMember(CrewMember member) throws NotAbleToUpdateException {
        SimpleCrewService service = SimpleCrewService.INSTANCE;
        if (service.isAssignedOnAnyMissions(member)) {
            String memberIsNotVacantMsg = "Member cannot be changed, because he's assigned at least on one mission";
            throw new NotAbleToUpdateException(memberIsNotVacantMsg);
        }
        CrewMember memberUpdate;
        EntityFactory<CrewMember> factory = CrewMemberFactory.getInstance();
        if (Math.random() * 100 > 50) {
            Role role = Role.resolveRoleById((int) Math.floor(Math.random() * Role.values().length + 1));
            memberUpdate = factory.create(member.getName(), role, member.getRank(),
                    member.isReadyForNextMissions(), member.getId());
        } else {
            Rank rank = Rank.resolveRankById((int) Math.floor(Math.random() * Rank.values().length + 1));
            memberUpdate = factory.create(member.getName(), member.getRole(), rank,
                    member.isReadyForNextMissions(), member.getId());
        }
        return service.updateCrewMemberDetails(memberUpdate);
    }
}
