package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaApplicationMenu;

// todo replace Object with your own types
@FunctionalInterface
public interface ApplicationMenu {

    void show();

    default void printAvailableOptions() {

    }

    default Object handleUserInput(String input) {
        return null;
    }

    static ApplicationMenu getApplicationMenu() {
        return new NassaApplicationMenu();
    }
}
