package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;

public class CrewMemberCriteriaBuilder extends CriteriaBuilder<CrewMember> {
    private Role role;
    private Rank rank;
    private Boolean isReadyForNextMissions;

    public CrewMemberCriteriaBuilder setRole(Role role) {
        this.role = role;
        return this;
    }

    public CrewMemberCriteriaBuilder setRank(Rank rank) {
        this.rank = rank;
        return this;
    }

    public CrewMemberCriteriaBuilder setReadyForNextMissions(Boolean readyForNextMissions) {
        isReadyForNextMissions = readyForNextMissions;
        return this;
    }

    @Override
    public CrewMemberCriteria getResult() {
        return new CrewMemberCriteria(id, name, role, rank, isReadyForNextMissions);
    }
}
