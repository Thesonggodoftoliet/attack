package com.littlepants.attack.attackplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.littlepants.attack.attackplus.entity.Campaign;
import com.littlepants.attack.attackplus.entity.Testcase;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 测试用例的顺序等 服务类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
public interface CampaignService extends IService<Campaign>{
    Map<String, List<Long>> getTestcaseIdsGroupByPlatform(Long campaignId);
    void addCampaign(Campaign campaign);
    void assignTestcases(Campaign campaign,List<Testcase> testcases);
    void updateCampaign(Campaign campaign);
    void deleteCampaignById(Long campaignId);
}
