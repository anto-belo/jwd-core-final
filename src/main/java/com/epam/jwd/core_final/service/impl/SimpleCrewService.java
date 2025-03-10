package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.exception.NoSuitableMissionsException;
import com.epam.jwd.core_final.exception.NotReadyForNextMissions;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.service.CrewService;
import com.epam.jwd.core_final.service.MissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.jwd.core_final.context.Application.context;

public enum SimpleCrewService implements CrewService {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCrewService.class);

    @Override
    public List<CrewMember> findAllCrewMembers() {
        return new ArrayList<>(context.retrieveBaseEntityList(CrewMember.class));
    }

    @Override
    public List<CrewMember> findAllCrewMembersByCriteria(Criteria<? extends CrewMember> criteria) {
        return context.retrieveBaseEntityList(CrewMember.class).stream()
                .filter(((CrewMemberCriteria) criteria)::isSuitsToCriteria)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CrewMember> findCrewMemberByCriteria(Criteria<? extends CrewMember> criteria) {
        return context.retrieveBaseEntityList(CrewMember.class).stream()
                .filter(((CrewMemberCriteria) criteria)::isSuitsToCriteria)
                .findFirst();
    }

    @Override
    public CrewMember updateCrewMemberDetails(CrewMember newCrewMember) {
        Long memberToUpdateId = newCrewMember.getId();
        Optional<CrewMember> oMember = getCrewMemberById(memberToUpdateId);
        String memberNotExistMsg = "Unable to update crew member object, because it does not exist: ";
        CrewMember currentCrewMember = oMember.orElseThrow(()
                -> new UnknownEntityException(memberNotExistMsg + newCrewMember.getName()));
        Collection<CrewMember> members = context.retrieveBaseEntityList(CrewMember.class);
        ((List<CrewMember>) members).set(((List<CrewMember>) members).indexOf(currentCrewMember),
                newCrewMember);
        return getCrewMemberById(memberToUpdateId).orElse(null);
    }

    public Optional<CrewMember> getCrewMemberById(Long id) {
        return context.retrieveBaseEntityList(CrewMember.class).stream()
                .filter(s -> id.equals(s.getId()))
                .findFirst();
    }

    @Override
    public void assignCrewMemberOnMission(CrewMember crewMember) throws RuntimeException {
        if (!crewMember.isReadyForNextMissions()) {
            String memberNotReadyMsg = "Member cannot be assigned on a mission, because he failed the last: ";
            throw new NotReadyForNextMissions(memberNotReadyMsg + crewMember.getName());
        }

        Optional<FlightMission> oMission = getSuitableMission(crewMember);
        FlightMission mission = oMission.orElseThrow(NoSuitableMissionsException::new);
        mission.getAssignedCrew().add(crewMember);
    }

    private Optional<FlightMission> getSuitableMission(CrewMember member) {
        return context.retrieveBaseEntityList(FlightMission.class).stream()
                .filter(s -> s.getAssignedSpaceship() != null)
                .filter(s -> getMembersNeeded(s.getAssignedSpaceship()) > s.getAssignedCrew().size())
                .filter(s -> !isIntersectsWithMemberMissions(s, member))
                .filter(s -> isMissionSpaceshipSuits(member.getRole(), s))
                .findFirst();
    }

    @Override
    public int getMembersNeeded(Spaceship spaceship) {
        int needed = 0;
        Map<Role, Short> crew = spaceship.getCrew();
        for (Role r : crew.keySet()) {
            needed += crew.get(r);
        }
        return needed;
    }

    private boolean isIntersectsWithMemberMissions(FlightMission currentMission, CrewMember member) {
        MissionService service = SimpleMissionService.INSTANCE;
        for (FlightMission mission : service.findAllMissions()) {
            if (!mission.equals(currentMission) && (mission.getMissionResult().equals(MissionResult.PLANNED)
                    || mission.getMissionResult().equals(MissionResult.IN_PROGRESS))
                    && mission.getAssignedCrew().contains(member)
                    && service.isIntersectingMissions(currentMission, mission))
                return true;
        }
        return false;
    }

    private boolean isMissionSpaceshipSuits(Role memberRole, FlightMission mission) {
        Spaceship spaceship = mission.getAssignedSpaceship();
        if (spaceship == null) {
            return false;
        }
        Short memberRoleLimit = spaceship.getCrew().get(memberRole);
        short memberRoleCurrent = (short) mission.getAssignedCrew().stream()
                .filter(s -> s.getRole() == memberRole)
                .count();
        return memberRoleCurrent < memberRoleLimit;
    }

    @Override
    public CrewMember createCrewMember(CrewMember crewMember) throws RuntimeException {
        if (!isUniqueName(crewMember.getName())) {
            String memberExistsMsg = "Crew member already exists";
            throw new EntityDuplicateException(memberExistsMsg);
        }
        try {
            return context.addEntity(crewMember);
        } catch (InvalidStateException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    private boolean isUniqueName(String name) {
        return context.retrieveBaseEntityList(CrewMember.class).stream()
                .noneMatch(s -> s.getName().equals(name));
    }

    public boolean isAssignedOnAnyMissions(CrewMember member) {
        List<FlightMission> missions = SimpleMissionService.INSTANCE.findAllMissions();
        if (missions.size() == 0) {
            return false;
        }
        return missions.stream()
                .filter(s -> s.getMissionResult() == MissionResult.PLANNED ||
                        s.getMissionResult() == MissionResult.IN_PROGRESS)
                .map(FlightMission::getAssignedCrew)
                .noneMatch(s -> s.contains(member));
    }
}
