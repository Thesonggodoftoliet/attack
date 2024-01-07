package com.littlepants.attack.attackweb.service.intf;

import com.littlepants.attack.attackweb.entity.Campaign;

import java.util.List;
import java.util.Map;

public interface CampaignService {
    int addCampaign(Campaign campaign);

    String addCampaignFromTemplate(String assessmentId,String campaignTempId);
    Campaign cloneCampaign(String campaignId,String assessmentId);
    int updateCampaign(Campaign campaign);
    int deleteCampaignById(String id);
    Campaign getCampaignById(String id);
    List<Campaign> getAllCampaign();
    List<Map<String,Object>> getTestcaseByCampaignId(String campaignId);
}
