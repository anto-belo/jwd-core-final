package com.epam.jwd.core_final.exception;

import java.util.Arrays;

public class UnknownEntityException extends RuntimeException {

    private final String entityName;
    private final Object[] args;

    public UnknownEntityException(String entityName) {
        super();
        this.entityName = entityName;
        this.args = null;
    }

    public UnknownEntityException(String entityName, Object[] args) {
        super();
        this.entityName = entityName;
        this.args = args;
    }

    @Override
    public String getMessage() {
        String baseMsg = "Unknown entity: " + entityName;
        if (args != null) {
            return baseMsg + " . Args: " + Arrays.toString(args);
        } else {
            return baseMsg;
        }
    }
}
