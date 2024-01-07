package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.littlepants.attack.attackweb.entity.Assessment;
import com.littlepants.attack.attackweb.entity.Campaign;
import com.littlepants.attack.attackweb.entity.CampaignTemplate;
import com.littlepants.attack.attackweb.entity.TestCase;
import com.littlepants.attack.attackweb.mapper.*;
import com.littlepants.attack.attackweb.service.intf.CampaignService;
import com.littlepants.attack.attackweb.service.intf.TestcaseService;
import com.littlepants.attack.attackweb.service.intf.TimeLineService;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final CampaignMapper campaignMapper;
    private final CampaignTemplateMapper campaignTemplateMapper;
    private final TestCaseMapper testCaseMapper;
    private final TestcaseService testcaseService;
    private final AssessmentMapper assessmentMapper;
    private final PhaseMapper phaseMapper;
    private final TechniqueMapper techniqueMapper;
    private final TimeLineService timeLineService;

    public CampaignServiceImpl(CampaignMapper campaignMapper, CampaignTemplateMapper campaignTemplateMapper,
                               TestCaseMapper testCaseMapper, TestcaseService testcaseService,
                               AssessmentMapper assessmentMapper, PhaseMapper phaseMapper,
                               TechniqueMapper techniqueMapper, TimeLineService timeLineService) {
        this.campaignMapper = campaignMapper;
        this.campaignTemplateMapper = campaignTemplateMapper;
        this.testCaseMapper = testCaseMapper;
        this.testcaseService = testcaseService;
        this.assessmentMapper = assessmentMapper;
        this.phaseMapper = phaseMapper;
        this.techniqueMapper = techniqueMapper;
        this.timeLineService = timeLineService;
    }

    @Override
    @Transactional
    public int addCampaign(Campaign campaign) {
        campaign.setId(UUIDGenerator.generateUUID());
        Timestamp timestamp = new Timestamp(new Date().getTime());
        campaign.setCreateTime(timestamp);
        campaign.setUpdateTime(timestamp);
        List<String> testcaseIds = new ArrayList<>();
        if (campaign.getTestcaseIds()!=null&&!campaign.getTestcaseIds().isEmpty()){
            List<String> testcaseTempIds = campaign.getTestcaseIds();
            String id;
            for (String testcaseTempId : testcaseTempIds) {
                try {
                    id = testcaseService.addTestCaseFromTemplate(testcaseTempId, campaign.getId());
                } catch (Exception e) {
                    throw new RuntimeException();
                }
                if (id != null)
                    testcaseIds.add(id);
            }
            campaign.setTestcaseIds(testcaseIds);
        }
        Assessment assessment = assessmentMapper.selectById(campaign.getAssessmentId());
        List<String> campaignIds = assessment.getCampaignIds();
        campaignIds.add(campaign.getId());
        assessment.setCampaignIds(campaignIds);
        assessment.setUpdateTime(timestamp);
        assessmentMapper.updateById(assessment);
        int tag;
        try {
            tag = campaignMapper.insert(campaign);
        }catch (Exception e){
            throw new RuntimeException();
        }
        return tag;
    }

    @Override
    @Transactional
    public String addCampaignFromTemplate(String assessmentId, String campaignTempId) {
        CampaignTemplate campaignTemplate = campaignTemplateMapper.selectById(campaignTempId);
        Campaign campaign = new Campaign();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        campaign.setTestcaseCounts(campaignTemplate.getTestcaseCounts());
        campaign.setCampaignName(campaignTemplate.getTemplateName());
        campaign.setCampaignDescription(campaignTemplate.getTemplateDescription());
        campaign.setUpdateTime(timestamp);
        campaign.setCreateTime(timestamp);
        campaign.setId(UUIDGenerator.generateUUID());
        List<String> testcaseIds = new ArrayList<>();
        if (campaignTemplate.getTestcaseIds()!=null&&!campaignTemplate.getTestcaseIds().isEmpty()){
            List<String> testcaseTempIds = campaignTemplate.getTestcaseIds();
            for (String testcaseTempId : testcaseTempIds) {
                String id;
                try {
                    id = testcaseService.addTestCaseFromTemplate(testcaseTempId, campaign.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
                if (id != null)
                    testcaseIds.add(id);
            }
            campaign.setTestcaseIds(testcaseIds);
        }
        campaign.setAssessmentId(assessmentId);
        int tag;
        try {
            tag = campaignMapper.insert(campaign);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        if (tag==0)
            return null;
        else
            return campaign.getId();
    }

    @Override
    @Transactional
    public Campaign cloneCampaign(String campaignId,String assessmentId) {
        Campaign campaign = campaignMapper.selectById(campaignId);
        List<TestCase> testCases = testCaseMapper.selectBatchIds(campaign.getTestcaseIds());
        Campaign newCampaign = new Campaign();
        newCampaign.setId(UUIDGenerator.generateUUID());
        newCampaign.setCampaignName(campaign.getCampaignName()+"副本");
        Timestamp timestamp = new Timestamp(new Date().getTime());
        newCampaign.setCreateTime(timestamp);
        newCampaign.setUpdateTime(timestamp);
        newCampaign.setAssessmentId(assessmentId);
        newCampaign.setCampaignDescription(campaign.getCampaignDescription());
        List<String> testcaseIds = new ArrayList<>();
        for (TestCase testCase:testCases){
            testCase.resetData();
            testCase.setCampaignId(newCampaign.getId());
            testcaseIds.add(testCase.getId());
            try {
                testCaseMapper.insert(testCase);
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        newCampaign.setTestcaseIds(testcaseIds);
        try {
            campaignMapper.insert(newCampaign);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        Assessment assessment = assessmentMapper.selectById(assessmentId);
        List<String> campaignIds = assessment.getCampaignIds();
        campaignIds.add(newCampaign.getId());
//        UpdateWrapper updateWrapper = new UpdateWrapper<>();
//        updateWrapper.set("campaign_ids",campaignIds);
//        updateWrapper.eq("id",assessmentId);
        assessment.setCampaignIds(campaignIds);
        assessment.setUpdateTime(timestamp);
//        assessmentMapper.update(null,updateWrapper);
        assessmentMapper.updateById(assessment);
        return newCampaign;
    }

    @Override
    public int updateCampaign(Campaign campaign) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
//        System.out.println(JsonUtils.toString(campaign));
        UpdateWrapper updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("campaign_name",campaign.getCampaignName());
        updateWrapper.set("campaign_description",campaign.getCampaignDescription());
        updateWrapper.set("update_time",timestamp);
        updateWrapper.eq("id",campaign.getId());
//        System.out.println(updateWrapper.getSqlSet());
        return campaignMapper.update(null,updateWrapper);
    }

    @Override
    @Transactional
    public int deleteCampaignById(String id) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Campaign campaign = campaignMapper.selectById(id);
//        System.out.println(JsonUtils.toString(campaign.getTestcaseIds()));
        int tag = testCaseMapper.deleteBatchIds(campaign.getTestcaseIds());
//        System.out.println(tag);
//        System.out.println(campaign.getTestcaseIds().size());
        Assessment assessment = assessmentMapper.selectById(campaign.getAssessmentId());
        List<String> campaignIds = assessment.getCampaignIds();
        campaignIds.remove(id);
        assessment.setUpdateTime(timestamp);
        assessment.setCampaignIds(campaignIds);
        assessmentMapper.updateById(assessment);
        try {
            timeLineService.deleteTimeLine(campaign.getId());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        if (tag!=campaign.getTestcaseIds().size())
            throw new RuntimeException();
        try {
            campaignMapper.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        return 1;
    }

    @Override
    public Campaign getCampaignById(String id) {
        return campaignMapper.selectById(id);
    }

    @Override
    public List<Campaign> getAllCampaign() {
        return campaignMapper.selectList(null);
    }

    @Override
    public List<Map<String, Object>> getTestcaseByCampaignId(String campaignId) {
        Campaign campaign = campaignMapper.selectById(campaignId);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("id","testcase_name","team_ids","JSON_UNQUOTE(json_extract(redteam,'$.phase')) as phase",
                "JSON_UNQUOTE(json_extract(redteam,'$.technique')) as technique","JSON_UNQUOTE(json_extract(redteam,'$.status')) as status",
                "JSON_UNQUOTE(json_extract(blueteam,'$.outcome')) as outcome");
        queryWrapper.in("id",campaign.getTestcaseIds());
        List<Map<String,Object>> templates = testCaseMapper.selectMaps(queryWrapper);
        for (Map val:templates){
            if (val.get("phase")!=null)
                val.put("phase",phaseMapper.selectById((Serializable) val.get("phase")).getName());
            if (val.get("technique")!=null&&techniqueMapper.selectById((Serializable) val.get("technique"))!=null)
                val.put("technique",techniqueMapper.selectById((Serializable) val.get("technique")).getLabel());
        }
        return templates;
    }
}
