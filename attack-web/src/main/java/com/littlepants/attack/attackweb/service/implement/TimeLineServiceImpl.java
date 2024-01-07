package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.littlepants.attack.attackweb.entity.TimeLine;
import com.littlepants.attack.attackweb.mapper.TimeLineMapper;
import com.littlepants.attack.attackweb.service.intf.TimeLineService;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class TimeLineServiceImpl implements TimeLineService {
    private final TimeLineMapper timeLineMapper;

    public TimeLineServiceImpl(TimeLineMapper timeLineMapper) {
        this.timeLineMapper = timeLineMapper;
    }

    @Override
    public int addTimeLine(TimeLine timeLine) {
        timeLine.setId(UUIDGenerator.generateUUID());
        Timestamp timestamp = new Timestamp(new Date().getTime());
        timeLine.setCreateTime(timestamp);
        return timeLineMapper.insert(timeLine);
    }

    @Override
    public int deleteTimeLine(String campaignId) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("campaign_id",campaignId);
        return timeLineMapper.delete(queryWrapper);
    }

    @Override
    public int deleteTimeLineByTestId(String testcaseId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("testcase_id",testcaseId);
        return timeLineMapper.delete(queryWrapper);
    }

    @Override
    public List<TimeLine> getTimeLines(String campaignId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("campaign_id",campaignId);
        queryWrapper.orderByDesc("create_time");
        return timeLineMapper.selectList(queryWrapper);
    }

    @Override
    public List<TimeLine> getTimeLineByCampaignId(String campaignId) {
        return timeLineMapper.getTimeLinesByCampaignId(campaignId);
    }
}
