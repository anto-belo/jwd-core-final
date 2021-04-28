package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.Menu;
import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.exception.InvalidFileFormatException;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.factory.impl.PlanetFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.util.impl.CrewFileParser;
import com.epam.jwd.core_final.util.impl.SpacemapFileParser;
import com.epam.jwd.core_final.util.impl.SpaceshipFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

// todo
public class NassaContext implements ApplicationContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(NassaContext.class);
    public static Menu chosenMenu = MainMenu.INSTANCE;
    public static int chosenOption;

    // no getters/setters for them
    private final Collection<CrewMember> crewMembers = new ArrayList<>();
    private final Collection<Spaceship> spaceships = new ArrayList<>();
    private final Collection<Planet> planetMap = new ArrayList<>();
    private final Collection<FlightMission> missions = new ArrayList<>();

    @SuppressWarnings("unchecked") //todo try to change
    @Override
    public <T extends BaseEntity> Collection<T> retrieveBaseEntityList(Class<T> tClass) {
        if (tClass.equals(CrewMember.class)) {
            return (Collection<T>) crewMembers;
        }
        if (tClass.equals(Spaceship.class)) {
            return (Collection<T>) spaceships;
        }
        if (tClass.equals(Planet.class)) {
            return (Collection<T>) planetMap;
        }
        if (tClass.equals(FlightMission.class)) {
            return (Collection<T>) missions;
        }
        return null;
    }

    /**
     * You have to read input files, populate collections
     */
    @Override
    public void init() throws InvalidStateException {
        try {
            new CrewFileParser().fillEntityStorage();
            new SpaceshipFileParser().fillEntityStorage();
            new SpacemapFileParser().fillEntityStorage();
        } catch (InvalidFileFormatException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
            throw new InvalidStateException("Unable to load start files");
        }
    }

    @SuppressWarnings("unchecked") //todo try to change
    @Override
    public <T extends BaseEntity> T addEntity(T entity) throws InvalidStateException {
        Long maxId = getMaxId(entity.getClass());
        Optional<EntityFactory<T>> oFactory = getFactory(entity);
        String cannotBeAddedToAnyCollectionMsg = "Entity cannot be added to any collection: ";
        EntityFactory<T> factory = oFactory.orElseThrow(()
                -> new InvalidStateException(cannotBeAddedToAnyCollectionMsg + entity.getName()));
        T entityWithId = factory.assignId(++maxId, entity);
        Collection<T> entities = (Collection<T>) retrieveBaseEntityList(entity.getClass());
        entities.add(entityWithId);
        return entityWithId;
    }

    private <T extends BaseEntity> Long getMaxId(Class<T> tClass) {
        return retrieveBaseEntityList(tClass).stream()
                .map(BaseEntity::getId)
                .max(Comparator.naturalOrder())
                .orElse(-1L);
    }

    @SuppressWarnings("unchecked") //todo try to change
    private <T extends BaseEntity> Optional<EntityFactory<T>> getFactory(T entity) {
        EntityFactory<T> factory = null;
        String className = entity.getClass().getName();
        if (className.equals(CrewMember.class.getName())) {
            factory = (EntityFactory<T>) CrewMemberFactory.getInstance();
        } else if (className.equals(FlightMission.class.getName())) {
            factory = (EntityFactory<T>) FlightMissionFactory.getInstance();
        } else if (className.equals(Planet.class.getName())) {
            factory = (EntityFactory<T>) PlanetFactory.getInstance();
        } else if (className.equals(Spaceship.class.getName())) {
            factory = (EntityFactory<T>) SpaceshipFactory.getInstance();
        }
        return (factory == null) ? Optional.empty() : Optional.of(factory);
    }
}
