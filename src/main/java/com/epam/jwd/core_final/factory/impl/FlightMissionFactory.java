package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.factory.EntityFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class FlightMissionFactory implements EntityFactory<FlightMission> {
    private static EntityFactory<FlightMission> factory;

    private FlightMissionFactory() {
    }

    public static EntityFactory<FlightMission> getInstance() {
        if (factory == null) {
            factory = new FlightMissionFactoryPreprocessor(new FlightMissionFactory());
        }
        return factory;
    }

    @Override
    public FlightMission create(Object... args) {
        String name = (String) args[0];
        LocalDateTime startDate = (LocalDateTime) args[1];
        Spaceship assignedSpaceship = (Spaceship) args[2];
        List<CrewMember> assignedCrew = (args[3] == null) ? new ArrayList<>() : (List<CrewMember>) args[3];
        MissionResult missionResult = (MissionResult) args[4];
        Planet from = (Planet) args[5];
        Planet to = (Planet) args[6];
        if (args.length == 7) {
            return new FlightMission(name, startDate, assignedSpaceship, assignedCrew,
                    missionResult, from, to);
        } else {
            Long id = (Long) args[7];
            return new FlightMission(id, name, startDate, assignedSpaceship, assignedCrew,
                    missionResult, from, to);
        }
    }

    @Override
    public FlightMission assignId(Long id, FlightMission entity) {
        return new FlightMission(id, entity.getName(), entity.getStartDate(), entity.getAssignedSpaceship(),
                entity.getAssignedCrew(), entity.getMissionResult(), entity.getFrom(), entity.getTo());
    }
}
