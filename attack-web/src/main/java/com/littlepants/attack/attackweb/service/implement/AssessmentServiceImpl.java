package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.littlepants.attack.attackweb.entity.Assessment;
import com.littlepants.attack.attackweb.entity.Campaign;
import com.littlepants.attack.attackweb.mapper.AssessmentMapper;
import com.littlepants.attack.attackweb.mapper.CampaignMapper;
import com.littlepants.attack.attackweb.service.intf.AssessmentService;
import com.littlepants.attack.attackweb.service.intf.CampaignService;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class AssessmentServiceImpl implements AssessmentService {
    private final AssessmentMapper assessmentMapper;
    private final CampaignMapper campaignMapper;
    private final CampaignService campaignService;

    public AssessmentServiceImpl(AssessmentMapper assessmentMapper, CampaignMapper campaignMapper, CampaignService campaignService) {
        this.assessmentMapper = assessmentMapper;
        this.campaignMapper = campaignMapper;
        this.campaignService = campaignService;
    }

    @Override
    @Transactional
    public int addAssessment(Assessment assessment) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        assessment.setCreateTime(timestamp);
        assessment.setUpdateTime(timestamp);
        assessment.setId(UUIDGenerator.generateUUID());
        List<String> campaignIds = new ArrayList<>();
        if (assessment.getCampaignIds()!=null&&!assessment.getCampaignIds().isEmpty()){
            List<String> campaignTempIds = assessment.getCampaignIds();
            for (String campaignTempId : campaignTempIds) {
                try {
                    String id = campaignService.addCampaignFromTemplate(assessment.getId(), campaignTempId);
                    if (id != null)
                        campaignIds.add(id);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
            assessment.setCampaignIds(campaignIds);
        }
        return assessmentMapper.insert(assessment);
    }

    @Override
    @Transactional
    public Assessment cloneAssessmentById(String assessmentId) {
        Assessment assessment = assessmentMapper.selectById(assessmentId);
        Assessment newAssessment = new Assessment();
        newAssessment.setId(UUIDGenerator.generateUUID());
        newAssessment.setAssessName(assessment.getAssessName()+"副本");
        newAssessment.setChainId(assessment.getChainId());
        Timestamp timestamp = new Timestamp(new Date().getTime());
        newAssessment.setCreateTime(timestamp);
        newAssessment.setUpdateTime(timestamp);
        List<Campaign> campaigns = campaignMapper.selectBatchIds(assessment.getCampaignIds());
        List<String>campaignIds = new ArrayList<>();
        for (Campaign campaign:campaigns){
            Campaign newCampaign = campaignService.cloneCampaign(campaign.getId(),newAssessment.getId());
            campaignIds.add(newCampaign.getId());
        }
        newAssessment.setCampaignIds(campaignIds);
        try {
            assessmentMapper.insert(newAssessment);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        return newAssessment;
    }

    @Override
    public int updateAssessment(Assessment assessment) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        assessment.setUpdateTime(timestamp);
        return assessmentMapper.updateById(assessment);
    }

    @Override
    @Transactional
    public int delAssessmentById(String assessmentId) {
        Assessment assessment = assessmentMapper.selectById(assessmentId);
        if(assessment.getCampaignIds()!=null)
            for (String campaignId:assessment.getCampaignIds()){
                campaignService.deleteCampaignById(campaignId);
            }
        try {
            assessmentMapper.deleteById(assessmentId);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        return 1;
    }

    @Override
    public Assessment getAssessmentById(String assessmentId) {
        return assessmentMapper.selectById(assessmentId);
    }

    @Override
    public List<Campaign> getCampaignsById(String assessmentId) {
        Assessment assessment = assessmentMapper.selectById(assessmentId);
        if (assessment.getCampaignIds().isEmpty())
            return new ArrayList<>();
        List<Campaign> campaignList = campaignMapper.selectBatchIds(assessment.getCampaignIds());
        for (Campaign campaign:campaignList){
            campaign.setTestcaseCounts(campaign.getTestcaseIds().size());
        }
        return campaignList;
    }

    @Override
    public List<Assessment> getAllAssessments() {
        return assessmentMapper.selectList(null);
    }

    @Override
    public List<Map<String, Object>> getCampaignsGroupByAssessment() {
        List<Assessment> assessments = getAllAssessments();
        List<Map<String,Object>>results = new ArrayList<>();
        for (Assessment assessment:assessments){
            Map<String,Object> map = new HashMap<>();
            List<Map<String,Object>> options = new ArrayList<>();
            List<String> campaignIds = assessment.getCampaignIds();
            List<Campaign> campaigns = campaignMapper.selectBatchIds(campaignIds);
            for (Campaign campaign:campaigns){
                Map<String,Object> option = new HashMap<>();
                option.put("value",campaign.getId());
                option.put("label",campaign.getCampaignName());
                options.add(option);
            }
            map.put("label",assessment.getAssessName());
            map.put("value",assessment.getId());
            map.put("children",options);
            results.add(map);
        }
        return results;
    }
}
