package com.littlepants.attack.attackplus.service.impl;

import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.dao.CampaignDao;
import com.littlepants.attack.attackplus.entity.Campaign;
import com.littlepants.attack.attackplus.entity.Testcase;
import com.littlepants.attack.attackplus.entity.TestcaseCaldera;
import com.littlepants.attack.attackplus.entity.TestcaseCampaign;
import com.littlepants.attack.attackplus.externalModel.caldera.Adversary;
import com.littlepants.attack.attackplus.service.CampaignService;
import com.littlepants.attack.attackplus.service.TestcaseCampaignService;
import com.littlepants.attack.attackplus.service.TestcaseStrategy;
import com.littlepants.attack.attackplus.utils.JsonUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 测试用例的顺序等 服务实现类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Service
public class CampaignServiceImpl extends ServiceImpl<CampaignDao,Campaign> implements CampaignService {
    private final TestcaseCampaignService testcaseCampaignService;
    private final CampaignDao campaignDao;
    private final ApplicationContext applicationContext;

    public CampaignServiceImpl(TestcaseCampaignService testcaseCampaignService, CampaignDao campaignDao, ApplicationContext applicationContext) {
        this.testcaseCampaignService = testcaseCampaignService;
        this.campaignDao = campaignDao;
        this.applicationContext = applicationContext;
    }

    /**
     * 获取该方案不同的平台分组
     * @param campaignId Long
     * @return Map<String, List<Long>>
     */
    @Override
    public Map<String, List<Long>> getTestcaseIdsGroupByPlatform(Long campaignId) {
        return testcaseCampaignService.getTestcaseGroupByPlatformByCampaignId(campaignId);
    }

    /**
     * 新增测试方案，并创建caldera Adversary文件
     * @param campaign Campaign
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    @Override
    public void addCampaign(Campaign campaign) {
        campaignDao.insert(campaign);
        Adversary adversary = new Adversary();
        adversary.setAdversary_id(String.valueOf(campaign.getId()));
        adversary.setDescription(campaign.getCampaignDescription());
        adversary.setName(campaign.getCampaignName());
        String url = "http://127.0.0.1:5000/caldera/adversaries";
        String result = HttpRequest.post(url)
                .body(JsonUtil.toString(adversary))
                .execute().body();
        System.out.println(result);
        //处理返回结果
    }

    /**
     * 给测试方案分配测试用例，并更新caldera Adversary文件
     * @param campaign Campaign
     * @param testcases Testcase列表
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    @Override
    public void assignTestcases(Campaign campaign, List<Testcase> testcases) {
        //没有调整顺序
        //没有对比数据库的testcase是否被插入或删除
        List<TestcaseCampaign> testcaseCampaigns = new ArrayList<>();
        List<Long> calderaIds = new ArrayList<>();
        List<Long> sqlIds = testcaseCampaignService.getTestcaseIdsByCampaignId(campaign.getId());
        if (sqlIds==null||sqlIds.isEmpty()) {//直接添加进数据库
            int i = 1;
            for (Testcase testcase:testcases){
                TestcaseCampaign testcaseCampaign = new TestcaseCampaign(campaign.getId(), testcase.getId(),
                        testcase.getPlatform(),i++);
                testcaseCampaigns.add(testcaseCampaign);

                // 获取Caldera的ability
                if (testcase.getPlatform().equals("caldera"))
                    calderaIds.add(testcase.getId());
            }
            testcaseCampaignService.saveBatch(testcaseCampaigns);
        }else {
            // 对比出插入和删除的ID
            Set<Long> testcaseIds = new HashSet<>();
            //新增
            int i = 1;
            for (Testcase testcase:testcases){
                testcaseIds.add(testcase.getId());
                if (!sqlIds.contains(testcase.getId())){
                    TestcaseCampaign testcaseCampaign = new TestcaseCampaign(campaign.getId(), testcase.getId(),
                            testcase.getPlatform(),i++);
                    testcaseCampaigns.add(testcaseCampaign);
                }

                // 获取Caldera的ability
                if (testcase.getPlatform().equals("caldera"))
                    calderaIds.add(testcase.getId());
            }
            testcaseCampaignService.saveBatch(testcaseCampaigns);
            //删除
            for (Long id:sqlIds){
                testcaseIds.remove(id);
            }
            testcaseCampaignService.removeByIds(testcaseIds);
        }

        // 获取Caldera Adversary
        String url = "http://127.0.0.1:5000/caldera/adversaries/"+campaign.getId();
        String result = HttpRequest.get(url)
                .execute().body();
        Adversary adversary = JsonUtil.toBean(result, Adversary.class);
        TestcaseStrategy testcaseStrategy = applicationContext.getBean("caldera",TestcaseStrategy.class);
        List<TestcaseCaldera> testcaseCalderas = testcaseStrategy.getTestcasesByIds(calderaIds);
        List<String> ids = new ArrayList<>();
        for (TestcaseCaldera caldera:testcaseCalderas)
            ids.add(caldera.getAbilityId());
        adversary.setAtomic_ordering(ids);
        result = HttpRequest.put(url)
                .body(JsonUtil.toString(adversary))
                .execute().body();
        System.out.println(result);
        //处理返回结果
    }

    /**
     * 更新campaign字段
     * @param campaign Campaign
     */
    @Override
    public void updateCampaign(Campaign campaign){
        Campaign newCampaign = new Campaign();
        newCampaign.setId(campaign.getId());
        newCampaign.setCampaignName(campaign.getCampaignName());
        newCampaign.setCampaignDescription(campaign.getCampaignDescription());
        campaignDao.updateById(newCampaign);
    }

    /**
     * 级联删除Campaign
     * @param campaignId Long
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    @Override
    public void deleteCampaignById(Long campaignId){
        List<Long> ids = testcaseCampaignService.getIdsByCampaignId(campaignId);
        testcaseCampaignService.removeBatchByIds(ids);
        campaignDao.deleteById(campaignId);
    }


}
