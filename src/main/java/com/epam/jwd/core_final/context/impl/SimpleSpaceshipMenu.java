package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.Menu;
import com.epam.jwd.core_final.context.Submenu;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteriaBuilder;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.NotAbleToUpdateException;
import com.epam.jwd.core_final.service.impl.SimpleMissionService;
import com.epam.jwd.core_final.service.impl.SimpleSpaceshipService;
import com.epam.jwd.core_final.util.UserImitator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SimpleSpaceshipMenu implements Submenu {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSpaceshipMenu.class);

    @Override
    public void printMenu() {
        System.out.println("+-----------------------------+\n"
                + "|      SPACESHIPS EDITOR      |\n"
                + "+-----------------------------+\n"
                + "| 1. Create spaceship         |\n"
                + "| 2. Edit spaceship           |\n"
                + "| 3. Show spaceships          |\n"
                + "| 4. Show spaceships          |\n"
                + "|    ready for missions       |\n"
                + "| /. Back                     |\n"
                + "+-----------------------------+");
    }

    @Override
    public void create() {
        System.out.println("Input spaceship name:");
        String input = Menu.getInput("(\\w+([ ]\\w+)*)|/", "Incorrect name.", false);
        if (input.equals("/")) {
            return;
        }
        try {
            UserImitator user = UserImitator.INSTANCE;
            Spaceship ship = user.generateShip(input);
            System.out.println("Generated ship:\n" + ship);
            Menu.getInput("/", "(/ -> back)", true);
        } catch (EntityDuplicateException e) {
            LOGGER.info(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void edit() {
        System.out.println("Input spaceship id to edit:");
        Application.context.retrieveBaseEntityList(Spaceship.class).forEach(System.out::println);
        Long id;
        SimpleSpaceshipService service = SimpleSpaceshipService.INSTANCE;
        while (true) {
            String input = Menu.getInput("([0-9]+)|/", "Incorrect id.", false);
            if (input.equals("/")) {
                return;
            }
            id = Long.parseLong(input);
            if (service.getSpaceshipById(id).isPresent()) {
                break;
            } else {
                System.out.println("There's no spaceships with such id.");
            }
        }
        try {
            UserImitator user = UserImitator.INSTANCE;
            Spaceship spaceship = user.updateSpaceship(service.getSpaceshipById(id).get());
            System.out.println("Updated spaceship:\n" + spaceship);
            Menu.getInput("/", "(/ -> back)", true);
        } catch (NotAbleToUpdateException e) {
            LOGGER.info(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void showAll() {
        SimpleMissionService.INSTANCE.updateMissionsStatus();
        System.out.println("All spaceships:");
        Application.context.retrieveBaseEntityList(Spaceship.class).forEach(System.out::println);
        Menu.getInput("/", "(/ -> back)", true);
    }

    @Override
    public void showSpecific() {
        SpaceshipCriteria criteria = new SpaceshipCriteriaBuilder().setReadyForNextMissions(true).getResult();
        SimpleSpaceshipService.INSTANCE.findAllSpaceshipsByCriteria(criteria).forEach(System.out::println);
        Menu.getInput("/", "(/ -> back)", true);
    }
}
