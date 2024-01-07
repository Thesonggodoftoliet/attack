package com.littlepants.attack.attackplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.littlepants.attack.attackplus.entity.TestcaseCampaign;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-07
 */
public interface TestcaseCampaignService extends IService<TestcaseCampaign>{
    Map<String, List<Long>> getTestcaseGroupByPlatformByCampaignId(Long campaignId);
    List<Long> getIdsByCampaignId(Long campaignId);
    List<String> getPlatformsByCampaignId(Long campaignId);
    List<Long> getTestcaseIdsByCampaignId(Long campaignId);
}
