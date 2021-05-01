package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.Menu;
import com.epam.jwd.core_final.context.Submenu;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteriaBuilder;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.NoVacantEntitiesException;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.impl.SimpleMissionService;
import com.epam.jwd.core_final.util.UserImitator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SimpleMissionMenu implements Submenu {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMissionMenu.class);

    @Override
    public void printMenu() {
        System.out.println("+-----------------------------+\n"
                + "|       MISSIONS EDITOR       |\n"
                + "+-----------------------------+\n"
                + "| 1. Create mission           |\n"
                + "| 2. Edit mission             |\n"
                + "| 3. Show missions            |\n"
                + "| 4. Show missions by status  |\n"
                + "| /. Back                     |\n"
                + "+-----------------------------+");
    }

    @Override
    public void create() {
        System.out.println("Input mission name:");
        String input = Menu.getInput("(\\w+([ ]\\w+)*)|/", "Incorrect name.", false);
        if (input.equals("/")) {
            return;
        }
        try {
            UserImitator user = UserImitator.INSTANCE;
            FlightMission mission = user.generateMission(input);
            System.out.println("Generated mission:\n" + mission);
            Menu.getInput("/", "(/ -> back)", true);
        } catch (EntityDuplicateException | NoVacantEntitiesException e) {
            LOGGER.info(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void edit() {
        System.out.println("Input mission id to edit:");
        Application.context.retrieveBaseEntityList(FlightMission.class).forEach(System.out::println);
        Long id;
        MissionService service = SimpleMissionService.INSTANCE;
        while (true) {
            String input = Menu.getInput("([0-9]+)|/", "Incorrect id.", false);
            if (input.equals("/")) {
                return;
            }
            id = Long.parseLong(input);
            if (service.getFlightMissionById(id).isPresent()) {
                break;
            } else {
                System.out.println("There's no missions with such id.");
            }
        }
        UserImitator user = UserImitator.INSTANCE;
        FlightMission mission = user.updateMission(service.getFlightMissionById(id).get());
        System.out.println("Updated mission:\n" + mission);
        Menu.getInput("/", "(/ -> back)", true);
    }

    @Override
    public void showAll() {
        SimpleMissionService.INSTANCE.updateMissionsStatus();
        System.out.println("All missions:");
        Application.context.retrieveBaseEntityList(FlightMission.class).forEach(System.out::println);
        Menu.getInput("/", "(/ -> back)", true);
    }

    @Override
    public void showSpecific() {
        System.out.println("Choose status:");
        for (MissionResult result : MissionResult.values()) {
            System.out.println(result.getId() + ". " + result);
        }
        int id;
        while (true) {
            String input = Menu.getInput("([0-9]+)|/", "Incorrect status code.", false);
            if (input.equals("/")) {
                return;
            }
            id = Integer.parseInt(input);
            if (id > MissionResult.values().length || id < 1) {
                System.out.println("There's no status with such code.");
            } else {
                break;
            }
        }
        FlightMissionCriteria criteria = new FlightMissionCriteriaBuilder()
                .setMissionResult(MissionResult.resolveResultById(id)).getResult();
        SimpleMissionService.INSTANCE.findAllMissionsByCriteria(criteria).forEach(System.out::println);
        Menu.getInput("/", "(/ -> back)", true);
    }
}
