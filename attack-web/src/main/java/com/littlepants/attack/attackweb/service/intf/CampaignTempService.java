package com.littlepants.attack.attackweb.service.intf;

import com.littlepants.attack.attackweb.entity.CampaignTemplate;

import java.util.List;
import java.util.Map;

public interface CampaignTempService {
    int addCampaignTemplate(CampaignTemplate campaignTemplate);
    int updateCampaignTemplate(CampaignTemplate campaignTemplate);
    int deleteCampaignTemplateById(String id);
    int cloneCampaignTemplateById(String id);
    CampaignTemplate getTemplateById(String id);
    List<CampaignTemplate> getAllTemplates();
    Map<String,Object> getTemplatesByPage(int page,int count);
    int createTemplateFromJSON();
    List<Map<String,Object>> getTestcaseTempByCampaignId(String campaignId);
}
