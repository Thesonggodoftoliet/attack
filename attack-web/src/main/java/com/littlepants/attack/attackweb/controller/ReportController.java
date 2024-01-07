package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.response.ResponseCode;
import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import com.littlepants.attack.attackweb.service.intf.AssessmentService;
import com.littlepants.attack.attackweb.service.intf.ReportService;
import com.littlepants.attack.attackweb.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;

@Controller
@RequestMapping("/logger")
public class ReportController {
    private final AssessmentService assessmentService;
    private final ReportService reportService;

    public ReportController(AssessmentService assessmentService, ReportService reportService) {
        this.assessmentService = assessmentService;
        this.reportService = reportService;
    }

    @GetMapping("/getOptions")
    @CrossOrigin
    @ResponseBody
    public String getOptions(){
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(assessmentService.getCampaignsGroupByAssessment())).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getMetricData")
    public String getMetricData(@RequestBody Map map){
        List<String> campaignIds = (List<String>) map.get("ids");
        Map<String,Object> results = new HashMap<>();
        results.put("overall",reportService.getTestcaseScore(campaignIds));
        results.put("blueTool",reportService.getBlueToolData(campaignIds));
        results.put("phase",reportService.getPhaseStatistics(campaignIds));
        results.put("ratesOfCampaign",reportService.getCampaignRates(campaignIds));
        results.put("ratesOfPhase",reportService.getPhasesRates(campaignIds));
        results.put("ratesOfTechnique",reportService.getTechniquesRates(campaignIds));
        return new CustomResponseEntity(ResponseCode.SUCCESS, JsonUtils.toString(results)).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getTrendingData")
    public String getTrendingData(@RequestBody Map map){
        List<String> campaignIds = (List<String>) map.get("ids");
        String granularity = (String) map.get("granularity");
//        Date endTime = (Date) map.get("endTime");
        Calendar calendar = Calendar.getInstance();
        Date endTime = new Date(calendar.getTimeInMillis());
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(reportService.getRatesByTime(campaignIds,granularity,endTime))).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getScoredData")
    public String getScoredData(@RequestBody Map map){
        List<String> campaignIds = (List<String>) map.get("ids");
        Map<String,Object> results = new HashMap<>();
        results.put("score",reportService.getEffectiveLayers(campaignIds));
        results.put("Top10",reportService.getTop10BlueTool(campaignIds));
        List<Map<String,Object>> campaign = reportService.getCampaignRates(campaignIds);
        Collections.sort(campaign,(Comparator.comparingDouble(o -> (Double) o.get("score"))));
        if (campaign.size()<=5){
            results.put("mostCampaign",campaign);
            results.put("leastCampaign",campaign);
        }else {
            List<Map<String,Object>> temp = new ArrayList<>();
            for (int i=campaign.size()-1;i>= campaign.size()-5;i--)
                temp.add(campaign.get(i));
            results.put("mostCampaign",temp);
            results.put("leastCampaign",campaign.subList(0,5));
        }
        List<Map<String,Object>> phase = reportService.getPhasesRates(campaignIds);
        Collections.sort(phase,(Comparator.comparingDouble(o -> Double.valueOf(o.get("score").toString()))));
        if (phase.size()<=5){
            results.put("mostPhase",phase);
            results.put("leastPhase",phase);
        }else {
            List<Map<String,Object>> temp = new ArrayList<>();
            for (int i=phase.size()-1;i>= phase.size()-5;i--)
                temp.add(phase.get(i));
            results.put("mostPhase",temp);
            results.put("leastPhase",phase.subList(0,5));
        }

        List<Map<String,Object>> technique = reportService.getTechniquesRates(campaignIds);
        Collections.sort(technique,(Comparator.comparingDouble(o ->  Double.valueOf(o.get("score").toString()))));
        if (technique.size()<=5){
            results.put("mostTechnique",technique);
            results.put("leastTechnique",technique);
        }else {
            List<Map<String,Object>> temp = new ArrayList<>();
            for (int i=technique.size()-1;i>= technique.size()-5;i--)
                temp.add(technique.get(i));
            results.put("mostTechnique",temp);
            results.put("leastTechnique",technique.subList(0,5));
        }
        results.put("phaseStatics",reportService.getPhaseStatistics(campaignIds));
        results.put("overall",reportService.getTestcaseScore(campaignIds));
        return new CustomResponseEntity(ResponseCode.SUCCESS,JsonUtils.toString(results)).toString();
    }
}
