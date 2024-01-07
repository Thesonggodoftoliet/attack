package com.littlepants.attack.attackweb.service.intf;

import com.littlepants.attack.attackweb.entity.Team;

import java.util.List;

public interface TeamService {
    List<Team> getAllTeam();
    int addTeam(Team team);
    void editTeam(Team team);
    void delTeam(Team team);

    List<String> getTeamNameBatchIds(List<String> teamIds);
}
