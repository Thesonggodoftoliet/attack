package com.littlepants.attack.attackweb.service.intf;

import com.littlepants.attack.attackweb.entity.Assessment;
import com.littlepants.attack.attackweb.entity.Campaign;

import java.util.List;
import java.util.Map;

public interface AssessmentService {
    int addAssessment(Assessment assessment);
    Assessment cloneAssessmentById(String assessmentId);
    int updateAssessment(Assessment assessment);
    int delAssessmentById(String assessmentId);
    Assessment getAssessmentById(String assessmentId);
    List<Campaign> getCampaignsById(String assessmentId);
    List<Assessment> getAllAssessments();
    List<Map<String,Object>> getCampaignsGroupByAssessment();
}
