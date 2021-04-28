package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.Menu;
import com.epam.jwd.core_final.util.MissionExporter;

import static com.epam.jwd.core_final.context.impl.NassaContext.*;

public enum MainMenu implements Menu {
    INSTANCE;

    @Override
    public void printMenu() {
        System.out.println("+---------------------------+");
        System.out.println("| NASSA MISSION CREATOR 1.0 |");
        System.out.println("+---------------------------+");
        System.out.println("| 1. Create mission         |");
        System.out.println("| 2. Edit mission           |");
        System.out.println("| 3. Show missions          |");
//        System.out.println("| 4. Create spaceship       |");
//        System.out.println("| 5. Edit spaceship         |");
//        System.out.println("| 6. Show spaceships        |");
//        System.out.println("| 7. Create crew member     |");
//        System.out.println("| 8. Edit crew member       |");
//        System.out.println("| 9. Show crew members      |");
        System.out.println("| 0. Export missions        |");
        System.out.println("| /. Exit                   |");
        System.out.println("+---------------------------+");
    }

    @Override
    public void handleInput() {
        System.out.print("> ");
        String input = inputSc.nextLine();
        if (!input.matches("[0-9]|/")) {
            return;
        }
        if (input.equals("/")) {
            chosenOption = -1;
            return;
        }
        switch (Integer.parseInt(input)) {
            case 1:
                chosenMenu = MissionMenu.INSTANCE;
                chosenOption = 1;
                break;
            case 2:
                chosenMenu = MissionMenu.INSTANCE;
                chosenOption = 2;
                break;
            case 3:
                chosenMenu = MissionMenu.INSTANCE;
                chosenOption = 3;
                break;
//            case 4:
//            case 5:
//            case 6:
//            case 7:
//            case 8:
//            case 9:
            case 0:
                MissionExporter exporter = MissionExporter.INSTANCE;
                exporter.export();
        }
    }
}
