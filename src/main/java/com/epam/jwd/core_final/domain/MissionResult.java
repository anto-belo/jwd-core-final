package com.epam.jwd.core_final.domain;

import com.epam.jwd.core_final.exception.UnknownEntityException;

public enum MissionResult implements BaseEntity {
    CANCELLED(1L),
    FAILED(2L),
    PLANNED(3L),
    IN_PROGRESS(4L),
    COMPLETED(5L);

    private final Long id;

    MissionResult(Long id) {
        this.id = id;
    }

    public static MissionResult resolveResultById(int id) {
        for (MissionResult r : MissionResult.values()) {
            if (r.getId() == id) {
                return r;
            }
        }
        throw new UnknownEntityException("Unknown result id: " + id);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name();
    }
}
