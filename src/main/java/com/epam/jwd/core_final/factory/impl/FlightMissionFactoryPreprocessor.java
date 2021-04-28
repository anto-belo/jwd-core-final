package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.InvalidArgumentException;
import com.epam.jwd.core_final.factory.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class FlightMissionFactoryPreprocessor implements EntityFactory<FlightMission> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlightMissionFactoryPreprocessor.class);
    private final EntityFactory<FlightMission> factory;

    FlightMissionFactoryPreprocessor(EntityFactory<FlightMission> factory) {
        this.factory = factory;
    }

    @Override
    public FlightMission create(Object... args) {
        try {
            if ((args.length != 7 && args.length != 8)
                    || !(args[0] != null && args[0] instanceof String)
                    || !(args[1] != null && args[1] instanceof LocalDateTime)
                    || !(args[2] == null || args[2] instanceof Spaceship)
                    || !(args[3] == null || args[3] instanceof List)
                    || !(args[4] != null && args[4] instanceof MissionResult)
                    || !(args[5] != null && args[5] instanceof Planet)
                    || !(args[6] != null && args[6] instanceof Planet)
                    || (args.length == 8
                    && !(args[7] != null && args[7] instanceof Long))) {
                throw new InvalidArgumentException(CANNOT_CREATE_OBJ_MSG + args[0]);
            }
            return factory.create(args);
        } catch (InvalidArgumentException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public FlightMission assignId(Long id, FlightMission entity) {
        return factory.assignId(id, entity);
    }
}
