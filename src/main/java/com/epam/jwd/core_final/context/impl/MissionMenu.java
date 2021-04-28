package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.Menu;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.NoVacantEntitiesException;
import com.epam.jwd.core_final.service.impl.SimpleMissionService;
import com.epam.jwd.core_final.util.UserImitator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static com.epam.jwd.core_final.context.impl.NassaContext.chosenMenu;
import static com.epam.jwd.core_final.context.impl.NassaContext.chosenOption;

public enum MissionMenu implements Menu {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(MissionMenu.class);

    @Override
    public void printMenu() {
        System.out.println("+---------------------------+");
        System.out.println("|      MISSIONS EDITOR      |");
        System.out.println("+---------------------------+");
        switch (chosenOption) {
            case 1:
                System.out.println("Input mission name:");
                break;
            case 2:
                System.out.println("Input mission id to edit:");
                printMissions();
                break;
            case 3:
                System.out.println("All missions:");
                printMissions();
                break;
        }
        System.out.println("(/ -> back)");
    }

    private void printMissions() {
        updateMissionsStatus();
        Application.context.retrieveBaseEntityList(FlightMission.class).stream()
                .map(s -> s.getId() + ": " + s.getName()
                        + " (" + s.getFrom().getName() + " - " + s.getTo().getName() + ") on "
                        + s.getAssignedSpaceship().getName() + " -> " + s.getMissionResult().name())
                .forEach(System.out::println);
    }

    private void updateMissionsStatus() {
        SimpleMissionService service = SimpleMissionService.INSTANCE;
        LocalDateTime now = LocalDateTime.now();
        for (FlightMission mission : Application.context.retrieveBaseEntityList(FlightMission.class)) {
            MissionResult res = mission.getMissionResult();
            if (mission.getEndDate().isBefore(now) && res != MissionResult.COMPLETED && res != MissionResult.FAILED) {
                service.changeMissionStatusEnded(mission);
            } else if (mission.getStartDate().isBefore(now) && now.isBefore(mission.getEndDate())
                    && res != MissionResult.IN_PROGRESS) {
                service.changeMissionStatusInProgress(mission);
            } else if (mission.getStartDate().isBefore(now)) {
                service.mayChangeMissionStatusCancelled(mission);
            }
        }
    }

    @Override
    public void handleInput() {
        System.out.print("> ");
        String input = inputSc.nextLine();
        if (input.equals("/")) {
            chosenMenu = MainMenu.INSTANCE;
            return;
        }

        UserImitator user = UserImitator.INSTANCE;
        switch (chosenOption) {
            case 1:
                while (!input.matches("\\w+([ ]\\w+)*")) {
                    System.out.println("Incorrect name");
                    System.out.print("> ");
                    input = inputSc.nextLine();
                }
                try {
                    FlightMission mission = user.generateMission(input);
                    System.out.println("Generated mission: ");
                    System.out.println(mission);
                    chosenMenu = MainMenu.INSTANCE;
                } catch (EntityDuplicateException | NoVacantEntitiesException e) {
                    LOGGER.info(e.getMessage());
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                SimpleMissionService service = SimpleMissionService.INSTANCE;
                while (!input.matches("[0-9]+")
                        || !service.getFlightMissionById(Long.parseLong(input)).isPresent()) {
                    System.out.println("There's no mission with such id.");
                    System.out.print("> ");
                    input = inputSc.nextLine();
                    if (input.equals("/")) {
                        chosenMenu = MainMenu.INSTANCE;
                        return;
                    }
                }
                FlightMission mission = user.updateMission(service.getFlightMissionById(Long.parseLong(input)).get());
                System.out.println("Updated mission: ");
                System.out.println(mission);
                break;
        }
    }
}
