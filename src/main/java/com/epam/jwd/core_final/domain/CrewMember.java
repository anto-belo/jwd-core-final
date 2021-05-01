package com.epam.jwd.core_final.domain;

import com.epam.jwd.core_final.service.impl.SimpleCrewService;

/**
 * Expected fields:
 * <p>
 * role {@link Role} - member role
 * rank {@link Rank} - member rank
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class CrewMember extends AbstractBaseEntity {
    private final Role role;
    private final Rank rank;
    private final Boolean isReadyForNextMissions;

    public CrewMember(String name, Role role, Rank rank) {
        super(name);
        this.role = role;
        this.rank = rank;
        this.isReadyForNextMissions = true;
    }

    public CrewMember(Long id, String name, Role role, Rank rank, Boolean isReadyForNextMissions) {
        super(id, name);
        this.role = role;
        this.rank = rank;
        this.isReadyForNextMissions = isReadyForNextMissions;
    }

    public Role getRole() {
        return role;
    }

    public Rank getRank() {
        return rank;
    }

    public Boolean isReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    @Override
    public String toString() {
        return String.format("#%-3d %-20s %-20s %-20s %-10s %s",
                id,
                name,
                role,
                rank,
                isReadyForNextMissions ? "READY" : "NOT READY",
                isReadyForNextMissions ?
                        (SimpleCrewService.INSTANCE.isAssignedOnAnyMissions(this) ? "VACANT" : "") : ""
        );
    }
}
