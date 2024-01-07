package com.littlepants.attack.attackplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackplus.entity.TestcaseCampaign;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-07
 */
@Mapper
public interface TestcaseCampaignDao extends BaseMapper<TestcaseCampaign> {
    @Select("SELECT DISTINCT platform FROM att_testcase_campaign WHERE campaign_id = #{campaign_id}")
    List<String> getPlatformsByCampaignId(@Param("campaign_id")Long campaignId);

    @Select("SELECT testcase_id FROM att_testcase_campaign WHERE campaign_id = #{campaign_id} AND platform = #{platform}")
    List<Long> getTestcaseIdsByCampaignAndPlatform(@Param("campaign_id")Long campaignId, @Param("platform")String platform);

    @Select("SELECT * FROM att_testcase_campaign WHERE campaign_id = #{campaign_id}")
    List<TestcaseCampaign> getTestcaseCampaignsByCampaignId(@Param("campaign_id")Long campaignId);

    @Select("SELECT id FROM att_testcase_campaign WHERE campaign_id = #{campaign_id}")
    List<Long> getIdsByCampaignId(@Param("campaign_id")Long campaignId);

    @Select("SELECT testcase_id FROM att_testcase_campaign WHERE campaign_id = #{campaign_id}")
    List<Long> getTestcaseIdsByCampaignId(@Param("campaign_id")Long campaignId);
}
