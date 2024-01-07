package com.littlepants.attack.attackweb.service.intf;


import com.littlepants.attack.attackweb.entity.AssessmentTemplate;
import com.littlepants.attack.attackweb.entity.CampaignTemplate;

import java.util.List;

public interface AssessmentTemplateService {
    int addTemplate(AssessmentTemplate assessmentTemplate);
    int updateTemplate(AssessmentTemplate assessmentTemplate);
    int deleteTemplate(AssessmentTemplate assessmentTemplate);
    AssessmentTemplate getTemplateById(String id);
    List<AssessmentTemplate> getAllTemplates();

    List<CampaignTemplate> getCampaignsTempById(String assessmentId);
}
