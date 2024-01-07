package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.entity.Campaign;
import com.littlepants.attack.attackweb.entity.TimeLine;
import com.littlepants.attack.attackweb.response.ResponseCode;
import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import com.littlepants.attack.attackweb.service.intf.CampaignService;
import com.littlepants.attack.attackweb.service.intf.TeamService;
import com.littlepants.attack.attackweb.service.intf.TimeLineService;
import com.littlepants.attack.attackweb.util.JsonUtils;
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
@RequestMapping("/logger")
public class CampaignController {
    private final CampaignService campaignService;
    private final TeamService teamService;
    private final TimeLineService timeLineService;

    public CampaignController(CampaignService campaignService, TeamService teamService, TimeLineService timeLineService) {
        this.campaignService = campaignService;
        this.teamService = teamService;
        this.timeLineService = timeLineService;
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getCampaign")
    public String getCampaignById(@RequestBody Map map){
        String id = (String) map.get("id");
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(campaignService.getCampaignById(id))).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getCampaignDetail")
    public String getCampaignDetail(@RequestBody Map map){
//        System.out.println(JsonUtils.toString(map));
        String id = (String) map.get("id");
        List<Map<String,Object>> testcaseLists = campaignService.getTestcaseByCampaignId(id);
        for (Map testcase:testcaseLists){
            if (testcase.get("team_ids") !=null) {
                List<String> teamNames = teamService.getTeamNameBatchIds((List<String>) testcase.get("team_ids"));
                testcase.put("teams", teamNames);
                testcase.remove("team_ids");
            }
        }
//        System.out.println(JsonUtils.toString(testcaseLists));
        return  new CustomResponseEntity(ResponseCode.SUCCESS,JsonUtils.toString(testcaseLists)).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/addCampaign")
    public String addCampaign(@RequestBody @Validated Campaign campaign, BindingResult result){
        List<Map<String,Object>> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Map<String,Object> msg = new HashMap<>();
                msg.put("error",error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        }
        try {
            campaignService.addCampaign(campaign);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","添加"+campaign.getCampaignName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PutMapping("/updateCampaign")
    public String updateCampaign(@RequestBody @Validated Campaign campaign,BindingResult result){
        List<Map<String,Object>> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Map<String,Object> msg = new HashMap<>();
                msg.put("error",error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        }
        try {
            campaignService.updateCampaign(campaign);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","修改"+campaign.getCampaignName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @DeleteMapping("/deleteCampaign")
    public String deleteCampaign(@RequestBody Campaign campaign){
        String id = campaign.getId();
        try {
            campaignService.deleteCampaignById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,
                    "删除"+campaign.getCampaignName()+"时出现错误，请重试！",null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/cloneCampaign")
    public String cloneCampaign(@RequestBody Campaign campaign){
        String id = campaign.getId();
        try {
            campaignService.cloneCampaign(id,campaign.getAssessmentId());
        }catch (DuplicateKeyException e){
            return new CustomResponseEntity(500,"行动名重复",null).toString();
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,
                    "克隆"+campaign.getCampaignName()+"时出现错误，请重试！",null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @PostMapping("/getTimeLine")
    @ResponseBody
    public String getCampaignTimeLine(@RequestBody Map map){
        String id = (String) map.get("id");
        List<TimeLine> timeLines = timeLineService.getTimeLines(id);
        return new CustomResponseEntity(ResponseCode.SUCCESS,JsonUtils.toString(timeLines)).toString();
    }
}
