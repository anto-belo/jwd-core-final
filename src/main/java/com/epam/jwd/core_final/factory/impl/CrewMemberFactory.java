package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.factory.EntityFactory;

// do the same for other entities
public final class CrewMemberFactory implements EntityFactory<CrewMember> {
    private static EntityFactory<CrewMember> factory;

    private CrewMemberFactory() {
    }

    public static EntityFactory<CrewMember> getInstance() {
        if (factory == null) {
            factory = new CrewMemberFactoryPreprocessor(new CrewMemberFactory());
        }
        return factory;
    }

    @Override
    public CrewMember create(Object... args) {
        String name = (String) args[0];
        Role role = (Role) args[1];
        Rank rank = (Rank) args[2];
        if (args.length == 3) {
            return new CrewMember(name, role, rank);
        } else {
            Boolean isReady = (Boolean) args[3];
            Long id = (Long) args[4];
            return new CrewMember(id, name, role, rank, isReady);
        }
    }

    @Override
    public CrewMember assignId(Long id, CrewMember entity) {
        return new CrewMember(id, entity.getName(), entity.getRole(), entity.getRank(), entity.isReadyForNextMissions());
    }
}
