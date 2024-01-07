package com.littlepants.attack.attackweb.service.intf;

import com.littlepants.attack.attackweb.entity.TimeLine;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TimeLineService {
    int addTimeLine(TimeLine timeLine);
    int deleteTimeLine(String campaignId);
    int deleteTimeLineByTestId(String testcaseId);
    List<TimeLine> getTimeLines(String campaignId);
    List<TimeLine> getTimeLineByCampaignId(String campaignId);
}
