package com.littlepants.attack.attackweb.service.intf;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface ReportService {
   Map<String,Object> getTestcaseScore(List<String> campaignIds);
   List<Map<String,Object>> getBlueToolData(List<String> campaignIds);
   List<Map<String,Object>> getCampaignRates(List<String> campaignIds);
   List<Map<String,Object>> getPhasesRates(List<String> campaignIds);
   List<Map<String,Object>> getTechniquesRates(List<String> campaignIds);
   List<Map<String,Object>> getRatesByTime(List<String> campaignIds, String granularity, Date endTime);
   Map<String,Object> getEffectiveLayers(List<String> campaignIds);
   List<Map<String, Object>> getTop10BlueTool(List<String> campaignIds);
   List<Map<String,Object>> getPhaseStatistics(List<String> campaignIds);
}
