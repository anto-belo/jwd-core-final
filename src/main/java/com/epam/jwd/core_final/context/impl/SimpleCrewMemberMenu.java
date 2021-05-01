package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.Menu;
import com.epam.jwd.core_final.context.Submenu;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.CrewMemberCriteriaBuilder;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.NotAbleToUpdateException;
import com.epam.jwd.core_final.service.CrewService;
import com.epam.jwd.core_final.service.impl.SimpleCrewService;
import com.epam.jwd.core_final.service.impl.SimpleMissionService;
import com.epam.jwd.core_final.util.UserImitator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SimpleCrewMemberMenu implements Submenu {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCrewMemberMenu.class);

    @Override
    public void printMenu() {
        System.out.println("+-----------------------------+\n"
                + "|       MEMBERS EDITOR        |\n"
                + "+-----------------------------+\n"
                + "| 1. Create member            |\n"
                + "| 2. Edit member              |\n"
                + "| 3. Show members             |\n"
                + "| 4. Show members by rank     |\n"
                + "| /. Back                     |\n"
                + "+-----------------------------+");
    }

    @Override
    public void create() {
        System.out.println("Input member name:");
        String input = Menu.getInput("(\\w+([ ]\\w+)*)|/", "Incorrect name.", false);
        if (input.equals("/")) {
            return;
        }
        try {
            UserImitator user = UserImitator.INSTANCE;
            CrewMember member = user.generateMember(input);
            System.out.println("Generated member:\n" + member);
            Menu.getInput("/", "(/ -> back)", true);
        } catch (EntityDuplicateException e) {
            LOGGER.info(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void edit() {
        System.out.println("Input member id to edit:");
        Application.context.retrieveBaseEntityList(CrewMember.class).forEach(System.out::println);
        Long id;
        SimpleCrewService service = SimpleCrewService.INSTANCE;
        while (true) {
            String input = Menu.getInput("([0-9]+)|/", "Incorrect id.", false);
            if (input.equals("/")) {
                return;
            }
            id = Long.parseLong(input);
            if (service.getCrewMemberById(id).isPresent()) {
                break;
            } else {
                System.out.println("There's no members with such id.");
            }
        }
        UserImitator user = UserImitator.INSTANCE;
        CrewMember member = null;
        try {
            member = user.updateCrewMember(service.getCrewMemberById(id).get());
        } catch (NotAbleToUpdateException e) {
            LOGGER.info(e.getMessage());
            System.out.println(e.getMessage());
        }
        System.out.println("Updated member:\n" + member);
        Menu.getInput("/", "(/ -> back)", true);
    }

    @Override
    public void showAll() {
        SimpleMissionService.INSTANCE.updateMissionsStatus();
        System.out.println("All members:");
        Application.context.retrieveBaseEntityList(CrewMember.class).forEach(System.out::println);
        Menu.getInput("/", "(/ -> back)", true);
    }

    @Override
    public void showSpecific() {
        System.out.println("Choose rank:");
        for (Rank r : Rank.values()) {
            System.out.println(r.getId() + ". " + r);
        }
        int id;
        while (true) {
            String input = Menu.getInput("([0-9]+)|/", "Incorrect rank code.", false);
            if (input.equals("/")) {
                return;
            }
            id = Integer.parseInt(input);
            if (id > Rank.values().length || id < 1) {
                System.out.println("There's no rank with such code.");
            } else {
                break;
            }
        }
        CrewMemberCriteria criteria = new CrewMemberCriteriaBuilder()
                .setRank(Rank.resolveRankById(id)).getResult();
        CrewService service = SimpleCrewService.INSTANCE;
        service.findAllCrewMembersByCriteria(criteria).forEach(System.out::println);
        Menu.getInput("/", "(/ -> back)", true);
    }
}
