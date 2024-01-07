package com.littlepants.attack.attackplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.dao.TestcaseCampaignDao;
import com.littlepants.attack.attackplus.entity.TestcaseCampaign;
import com.littlepants.attack.attackplus.service.TestcaseCampaignService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-07
 */
@Service
public class TestcaseCampaignServiceImpl extends ServiceImpl<TestcaseCampaignDao, TestcaseCampaign> implements TestcaseCampaignService {
    private final TestcaseCampaignDao testcaseCampaignDao;

    public TestcaseCampaignServiceImpl(TestcaseCampaignDao testcaseCampaignDao) {
        this.testcaseCampaignDao = testcaseCampaignDao;
    }

    /**
     * 根据CampaignId查询TestcaseId,该Id根据平台分组
     * @param campaignId Long
     * @return Map<String,List<Long>> <平台，Ids>
     */
    @Override
    public Map<String, List<Long>> getTestcaseGroupByPlatformByCampaignId(Long campaignId) {
        Map<String,List<Long>> testcaseIds = new HashMap<>();
        List<TestcaseCampaign> list = testcaseCampaignDao.getTestcaseCampaignsByCampaignId(campaignId);
        for (TestcaseCampaign testcaseCampaign:list){
            if (!testcaseIds.containsKey(testcaseCampaign.getPlatform())){
                List<Long> ids = new ArrayList<>();
                ids.add(testcaseCampaign.getTestcaseId());
                testcaseIds.put(testcaseCampaign.getPlatform(),ids);
            }else {
                List<Long> ids = testcaseIds.get(testcaseCampaign.getPlatform());
                ids.add(testcaseCampaign.getTestcaseId());
            }
        }
        return testcaseIds;
    }

    @Override
    public List<Long> getIdsByCampaignId(Long campaignId) {
        return testcaseCampaignDao.getIdsByCampaignId(campaignId);
    }

    @Override
    public List<String> getPlatformsByCampaignId(Long campaignId) {
        return testcaseCampaignDao.getPlatformsByCampaignId(campaignId);
    }

    @Override
    public List<Long> getTestcaseIdsByCampaignId(Long campaignId) {
        return testcaseCampaignDao.getTestcaseIdsByCampaignId(campaignId);
    }
}
