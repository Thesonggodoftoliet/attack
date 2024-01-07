package com.littlepants.attack.attackweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackweb.entity.TimeLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TimeLineMapper extends BaseMapper<TimeLine> {
    @Select("SELECT activitytimeline.*,testcase.testcase_name FROM activitytimeline,testcase WHERE " +
            "activitytimeline.campaign_id = #{campaignId} AND activitytimeline.testcase_id = testcase.id")
    List<TimeLine> getTimeLinesByCampaignId(@Param("campaignId") String campaignId);
}
