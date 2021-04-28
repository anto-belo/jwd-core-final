package com.epam.jwd.core_final.context;

import java.util.Scanner;

public interface Menu {
    Scanner inputSc = new Scanner(System.in);

    void printMenu();

    void handleInput();
}
