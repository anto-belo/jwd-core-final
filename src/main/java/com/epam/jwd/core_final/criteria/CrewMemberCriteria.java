package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.AbstractBaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.CrewMember} fields
 */
public class CrewMemberCriteria extends Criteria<CrewMember> {
    private final Role role;
    private final Rank rank;
    private final Boolean isReadyForNextMissions;

    CrewMemberCriteria(Long id, String name, Role role, Rank rank, Boolean isReadyForNextMissions) {
        super(id, name);
        this.role = role;
        this.rank = rank;
        this.isReadyForNextMissions = isReadyForNextMissions;
    }

    @Override
    public boolean isSuitsToCriteria(AbstractBaseEntity entity) {
        CrewMember cm = (CrewMember) entity;
        return super.isSuitsToCriteria(entity)
                && (role == null || role.equals(cm.getRole()))
                && (rank == null || rank.equals(cm.getRank()))
                && (isReadyForNextMissions == null || isReadyForNextMissions == cm.isReadyForNextMissions());
    }

}
