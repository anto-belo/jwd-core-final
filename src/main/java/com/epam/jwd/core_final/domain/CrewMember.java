package com.epam.jwd.core_final.domain;

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

    public CrewMember(String name, Role role, Rank rank, Boolean isReadyForNextMissions) {
        super(name);
        this.role = role;
        this.rank = rank;
        this.isReadyForNextMissions = isReadyForNextMissions;
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
        return "CrewMember{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", rank=" + rank +
                ", isReadyForNextMissions=" + isReadyForNextMissions +
                '}';
    }
}
