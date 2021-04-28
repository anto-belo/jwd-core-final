package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationMenu;

public class NassaApplicationMenu implements ApplicationMenu {
    @Override
    public void show() {
        do {
            NassaContext.chosenMenu.printMenu();
            NassaContext.chosenMenu.handleInput();
        } while (NassaContext.chosenOption != -1);
    }
}
