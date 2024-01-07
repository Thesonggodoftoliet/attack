package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.entity.Team;
import com.littlepants.attack.attackweb.response.ResponseCode;
import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import com.littlepants.attack.attackweb.service.intf.TeamService;
import com.littlepants.attack.attackweb.util.JsonUtils;
import com.littlepants.attack.attackweb.validation.TeamInfoValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @CrossOrigin
    @GetMapping("/getTeamInfo")
    @ResponseBody
    public String getAllTeamInfo() {
        List<Team> teams = teamService.getAllTeam();
        return new CustomResponseEntity(ResponseCode.SUCCESS, JsonUtils.toString(teams)).toString();
    }

    @CrossOrigin
    @PostMapping("/addTeam")
    @ResponseBody
    public String addTeam(@RequestBody @Validated(TeamInfoValidation.class) Team team, BindingResult result) {
        List<Map<String, Object>> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Map<String, Object> msg = new HashMap<>();
                msg.put("error", error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500, JsonUtils.toString(errors),
                    null).msgToString();
        }
        try {
            teamService.addTeam(team);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            Map<String, Object> msg = new HashMap<>();
            msg.put("error", "队伍名重复");
            errors.add(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> msg = new HashMap<>();
            msg.put("error", "添加" + team.getTeamName() + "时出现未知错误，请重试");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500, JsonUtils.toString(errors),
                    null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS, null).toString();
    }

    @CrossOrigin
    @PostMapping("/delTeam")
    @ResponseBody
    public String delTeam(@RequestBody Team team) {
        List<String> errors = new ArrayList<>();
        try {
            teamService.delTeam(team);
        } catch (Exception e) {
            errors.add("删除" + team.getTeamName() + "时出现未知错误，请重试");
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500, errors.toString(), null).toString();
        return new CustomResponseEntity(ResponseCode.SUCCESS, null).toString();
    }

    @CrossOrigin
    @PostMapping("/editTeam")
    @ResponseBody
    public String editTeam(@RequestBody @Validated(TeamInfoValidation.class) Team team,
                           BindingResult result) {
        List<Map<String, Object>> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Map<String, Object> msg = new HashMap<>();
                msg.put("error", error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500, JsonUtils.toString(errors),
                    null).msgToString();
        }
        try {
            teamService.editTeam(team);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            Map<String, Object> msg = new HashMap<>();
            msg.put("error", "队伍名重复");
            errors.add(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> msg = new HashMap<>();
            msg.put("error", "修改" + team.getTeamName() + "时出现未知错误，请重试");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500, JsonUtils.toString(errors),
                    null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS, null).toString();
    }
}
