package com.littlepants.attack.attackweb.service.implement;

import com.littlepants.attack.attackweb.entity.Team;
import com.littlepants.attack.attackweb.mapper.TeamMapper;
import com.littlepants.attack.attackweb.service.intf.TeamService;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamMapper teamMapper;

    public TeamServiceImpl(TeamMapper teamMapper) {
        this.teamMapper = teamMapper;
    }

    @Override
    public List<Team> getAllTeam() {
        return teamMapper.selectList(null);
    }

    @Override
    public int addTeam(Team team) {
        team.setId(UUIDGenerator.generateUUID());
        Timestamp timestamp = new Timestamp(new Date().getTime());
        team.setUpdateTime(timestamp);
        team.setCreateTime(timestamp);
        return teamMapper.insert(team);
    }

    @Override
    public void editTeam(Team team) {
        Team oldTeam = teamMapper.selectById(team.getId());
        if (team.getTeamName() != null)
            oldTeam.setTeamName(team.getTeamName());
        if (team.getAbbreviation() != null)
            oldTeam.setAbbreviation(team.getAbbreviation());
        if (team.getTeamDescription() != null)
            oldTeam.setTeamDescription(team.getTeamDescription());
        if (team.getUrl() != null)
            oldTeam.setUrl(team.getUrl());
        Timestamp timestamp = new Timestamp(new Date().getTime());
        oldTeam.setUpdateTime(timestamp);
        try {
            teamMapper.updateById(oldTeam);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delTeam(Team team) {
        teamMapper.deleteById(team);
    }

    @Override
    public List<String> getTeamNameBatchIds(List<String> teamIds) {
        List<Team> teams = teamMapper.selectBatchIds(teamIds);
        List<String> teamNames = new ArrayList<>();
        for (Team team : teams) {
            teamNames.add(team.getTeamName());
        }
        return teamNames;
    }
}
