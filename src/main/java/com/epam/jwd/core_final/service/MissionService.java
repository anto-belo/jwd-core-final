package com.epam.jwd.core_final.service;

import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.FlightMission;

import java.util.List;
import java.util.Optional;

public interface MissionService {

    List<FlightMission> findAllMissions();

    List<FlightMission> findAllMissionsByCriteria(Criteria<? extends FlightMission> criteria);

    Optional<FlightMission> findMissionByCriteria(Criteria<? extends FlightMission> criteria);

    FlightMission updateMissionDetails(FlightMission flightMission);

    FlightMission createMission(FlightMission flightMission);

    Optional<FlightMission> getFlightMissionById(Long id);

    boolean isIntersectingMissions(FlightMission m1, FlightMission m2);
}
