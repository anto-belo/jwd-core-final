package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationMenu;

import static com.epam.jwd.core_final.context.impl.NassaContext.chosenMenu;

public class NassaApplicationMenu implements ApplicationMenu {
    public static boolean exit;

    @Override
    public void show() {
        do {
            chosenMenu.printMenu();
            chosenMenu.handleInput();
        } while (!exit);
    }
}
