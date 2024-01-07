package com.littlepants.attack.attackweb.service.implement;

import com.littlepants.attack.attackweb.entity.AssessmentTemplate;
import com.littlepants.attack.attackweb.entity.CampaignTemplate;
import com.littlepants.attack.attackweb.mapper.AssessmentTemplateMapper;
import com.littlepants.attack.attackweb.mapper.CampaignTemplateMapper;
import com.littlepants.attack.attackweb.service.intf.AssessmentTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssessmentTempServiceImpl implements AssessmentTemplateService {
    @Autowired
    private AssessmentTemplateMapper assessmentTemplateMapper;
    @Autowired
    private CampaignTemplateMapper campaignTemplateMapper;
    @Override
    public int addTemplate(AssessmentTemplate assessmentTemplate) {
        return assessmentTemplateMapper.insert(assessmentTemplate);
    }

    @Override
    public int updateTemplate(AssessmentTemplate assessmentTemplate) {
        return assessmentTemplateMapper.updateById(assessmentTemplate);
    }

    @Override
    public int deleteTemplate(AssessmentTemplate assessmentTemplate) {
        return assessmentTemplateMapper.deleteById(assessmentTemplate.getId());
    }

    @Override
    public AssessmentTemplate getTemplateById(String id) {
        return assessmentTemplateMapper.selectById(id);
    }

    @Override
    public List<AssessmentTemplate> getAllTemplates() {
        List<AssessmentTemplate> templates = assessmentTemplateMapper.selectList(null);
        for (AssessmentTemplate assessmentTemplate : templates) {
            assessmentTemplate.setCampaignCounts(assessmentTemplate.getCampaignIds().size());
        }
        return templates;
    }

    @Override
    public List<CampaignTemplate> getCampaignsTempById(String assessmentId) {
        AssessmentTemplate assessmentTemplate = assessmentTemplateMapper.selectById(assessmentId);
        List<String> campaignIds = assessmentTemplate.getCampaignIds();
        List<CampaignTemplate> campaignTemplates = campaignTemplateMapper.selectBatchIds(campaignIds);
        for (CampaignTemplate campaignTemplate : campaignTemplates) {
            campaignTemplate.setTestcaseCounts(campaignTemplate.getTestcaseIds().size());
        }
        return campaignTemplates;
    }

}
