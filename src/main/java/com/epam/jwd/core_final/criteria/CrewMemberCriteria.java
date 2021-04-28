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

    private CrewMemberCriteria(Long id, String name, Role role, Rank rank, Boolean isReadyForNextMissions) {
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

    static class CrewMemberCriteriaBuilder extends CriteriaBuilder<CrewMember> {
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
        public Criteria<CrewMember> getResult() {
            return new CrewMemberCriteria(id, name, role, rank, isReadyForNextMissions);
        }
    }
}
