package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.exception.InvalidStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Application {

    Logger LOGGER = LoggerFactory.getLogger(Application.class);
    ApplicationContext context = new NassaContext();

    static void start() {
        // todo
        try {
            context.init();
        } catch (InvalidStateException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        ApplicationMenu.getApplicationMenu().show();
    }
}
