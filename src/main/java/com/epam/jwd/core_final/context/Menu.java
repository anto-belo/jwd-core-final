package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.MainMenu;
import com.epam.jwd.core_final.context.impl.NassaContext;

import java.util.Scanner;

public interface Menu {
    static String getInput(String regex, String msgIfIncorrect, boolean emptyAllowed) {
        Scanner sc = new Scanner(System.in);
        System.out.print("> ");
        String input = sc.nextLine();
        while (!input.matches(regex) && !(emptyAllowed && input.equals(""))) {
            if (msgIfIncorrect != null) {
                System.out.println(msgIfIncorrect);
            }
            System.out.print("> ");
            input = sc.nextLine();
        }
        return input;
    }

    default void handleInput() {
        Submenu s;
        if (this instanceof Submenu) {
            s = (Submenu) this;
        } else {
            return;
        }
        int option;
        do {
            String input = Menu.getInput("[0-9]{1,2}|/", null, false);
            if (input.equals("/")) {
                NassaContext.chosenMenu = MainMenu.INSTANCE;
                return;
            }
            option = Integer.parseInt(input);
        } while (option > 4 || option < 1);
        switch (option) {
            case 1:
                s.create();
                break;
            case 2:
                s.edit();
                break;
            case 3:
                s.showAll();
                break;
            case 4:
                s.showSpecific();
                break;
        }
    }

    void printMenu();
}
