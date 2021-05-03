package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.service.MissionStatusChanger;

import java.time.LocalDateTime;

public enum SimpleMissionStatusChanger implements MissionStatusChanger {
    INSTANCE;

    @Override
    public void update(FlightMission mission) {
        LocalDateTime now = LocalDateTime.now();
        MissionResult res = mission.getMissionResult();
        if (res == MissionResult.CANCELLED) {
            return;
        }
        int membersNeeded = SimpleCrewService.INSTANCE.getMembersNeeded(mission.getAssignedSpaceship());
        int membersCurrent = mission.getAssignedCrew().size();

        if (mission.getStartDate().isBefore(now) && membersNeeded != membersCurrent) {
            cancel(mission);
        } else if (mission.getEndDate().isBefore(now) && res != MissionResult.COMPLETED && res != MissionResult.FAILED) {
            end(mission);
        } else if (mission.getStartDate().isBefore(now) && now.isBefore(mission.getEndDate())
                && res != MissionResult.IN_PROGRESS) {
            inProgress(mission);
        } else if (now.isBefore(mission.getStartDate())) {
            mayCancel(mission);
        }
    }
}
