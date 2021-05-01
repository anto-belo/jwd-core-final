package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.Menu;
import com.epam.jwd.core_final.util.Exporter;
import com.epam.jwd.core_final.util.impl.MissionExporter;

import static com.epam.jwd.core_final.context.impl.NassaContext.chosenMenu;

public enum MainMenu implements Menu {
    INSTANCE;

    @Override
    public void printMenu() {
        System.out.println("+-----------------------------+\n"
                + "|  NASSA MISSION CREATOR 1.0  |\n"
                + "+-----------------------------+\n"
                + "| 1. Missions editor          |\n"
                + "| 2. Spaceships editor        |\n"
                + "| 3. Crew members editor      |\n"
                + "| 0. Export missions          |\n"
                + "| /. Exit                     |\n"
                + "+-----------------------------+");
    }

    @Override
    public void handleInput() {
        int option;
        do {
            String input = Menu.getInput("[0-9]{1,2}|/", null, false);
            if (input.equals("/")) {
                NassaApplicationMenu.exit = true;
                return;
            }
            option = Integer.parseInt(input);
        } while (option > 3 || option < 0);
        switch (option) {
            case 1:
                chosenMenu = SimpleMissionMenu.INSTANCE;
                break;
            case 2:
                chosenMenu = SimpleSpaceshipMenu.INSTANCE;
                break;
            case 3:
                chosenMenu = SimpleCrewMemberMenu.INSTANCE;
                break;
            case 0:
                Exporter exporter = MissionExporter.INSTANCE;
                exporter.export();
                break;
        }
    }
}
