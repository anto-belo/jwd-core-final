package com.epam.jwd.core_final.service;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.impl.SimpleCrewService;
import com.epam.jwd.core_final.service.impl.SimpleMissionService;
import com.epam.jwd.core_final.service.impl.SimpleSpaceshipService;

public interface MissionStatusChanger {
    default void end(FlightMission mission) {
        EntityFactory<FlightMission> factory = FlightMissionFactory.getInstance();
        int failFactor = 25;
        MissionResult result = (Math.random() * 100 < failFactor) ? MissionResult.FAILED : MissionResult.COMPLETED;
        if (result == MissionResult.FAILED) {
            setMissionersNotReady(mission);
        }
        FlightMission updatedMission = factory.create(mission.getName(), mission.getStartDate(),
                mission.getAssignedSpaceship(), mission.getAssignedCrew(), result,
                mission.getFrom(), mission.getTo(), mission.getId());
        SimpleMissionService.INSTANCE.updateMissionDetails(updatedMission);
    }

    default void inProgress(FlightMission mission) {
        EntityFactory<FlightMission> factory = FlightMissionFactory.getInstance();
        FlightMission missionUpdate = factory.create(mission.getName(), mission.getStartDate(),
                mission.getAssignedSpaceship(), mission.getAssignedCrew(), MissionResult.IN_PROGRESS,
                mission.getFrom(), mission.getTo(), mission.getId());
        SimpleMissionService.INSTANCE.updateMissionDetails(missionUpdate);
    }

    default void cancel(FlightMission mission) {
        EntityFactory<FlightMission> factory = FlightMissionFactory.getInstance();
        FlightMission missionUpdate = factory.create(mission.getName(), mission.getStartDate(),
                null, null, MissionResult.CANCELLED, mission.getFrom(), mission.getTo(), mission.getId());
        SimpleMissionService.INSTANCE.updateMissionDetails(missionUpdate);
    }

    default void mayCancel(FlightMission mission) {
        int cancelFactor = 10;
        if (Math.random() * 100 < cancelFactor) {
            cancel(mission);
        }
    }

    default void setMissionersNotReady(FlightMission mission) {
        EntityFactory<CrewMember> crewFactory = CrewMemberFactory.getInstance();
        CrewService crewService = SimpleCrewService.INSTANCE;
        for (CrewMember member : mission.getAssignedCrew()) {
            CrewMember memberUpdate = crewFactory.create(member.getName(), member.getRole(), member.getRank(),
                    false, member.getId());
            crewService.updateCrewMemberDetails(memberUpdate);
        }

        Spaceship spaceship = mission.getAssignedSpaceship();
        EntityFactory<Spaceship> spaceshipFactory = SpaceshipFactory.getInstance();
        Spaceship spaceshipUpdate = spaceshipFactory.create(spaceship.getName(), spaceship.getCrew(),
                spaceship.getFlightDistance(), false, spaceship.getId());
        SpaceshipService spaceshipService = SimpleSpaceshipService.INSTANCE;
        spaceshipService.updateSpaceshipDetails(spaceshipUpdate);
    }

    void update(FlightMission mission);
}
