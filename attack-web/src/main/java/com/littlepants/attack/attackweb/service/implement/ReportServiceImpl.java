package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.littlepants.attack.attackweb.entity.*;
import com.littlepants.attack.attackweb.mapper.*;
import com.littlepants.attack.attackweb.service.intf.ReportService;
import com.littlepants.attack.attackweb.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {
    private final TestCaseMapper testCaseMapper;
    private final CampaignMapper campaignMapper;
    private final ToolMapper toolMapper;
    private final PhaseMapper phaseMapper;
    private final AssessmentMapper assessmentMapper;
    private final TechniqueMapper techniqueMapper;
    private final LayerMapper layerMapper;

    public ReportServiceImpl(TestCaseMapper testCaseMapper, CampaignMapper campaignMapper, ToolMapper toolMapper,
                             PhaseMapper phaseMapper, AssessmentMapper assessmentMapper,
                             TechniqueMapper techniqueMapper, LayerMapper layerMapper) {
        this.testCaseMapper = testCaseMapper;
        this.campaignMapper = campaignMapper;
        this.toolMapper = toolMapper;
        this.phaseMapper = phaseMapper;
        this.assessmentMapper = assessmentMapper;
        this.techniqueMapper = techniqueMapper;
        this.layerMapper = layerMapper;
    }

    /**
     * 返回测试用例的统计数据
     *
     * @param campaignIds
     * @return Map
     */
    @Override
    public Map<String, Object> getTestcaseScore(List<String> campaignIds) {
        Map<String, Object> result = new HashMap<>();
        result.put("CountOfCampaigns", campaignIds.size());
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.in("campaign_id", campaignIds);
        queryWrapper.eq("JSON_UNQUOTE(json_extract(redteam,'$.status'))", 3);//已完成
        result.put("CountOfCompleted", testCaseMapper.selectCount(queryWrapper));
        queryWrapper.clear();
        queryWrapper.in("campaign_id", campaignIds);
        queryWrapper.eq("JSON_UNQUOTE(json_extract(blueteam,'$.outcome'))", 1);//已封锁
        result.put("CountOfBlocked", testCaseMapper.selectCount(queryWrapper));
        queryWrapper.clear();
        queryWrapper.in("campaign_id", campaignIds);
        queryWrapper.eq("JSON_UNQUOTE(json_extract(blueteam,'$.outcome'))", 2);//已侦测
        result.put("CountOfDetected", testCaseMapper.selectCount(queryWrapper));
        queryWrapper.clear();
        queryWrapper.in("campaign_id", campaignIds);
        queryWrapper.eq("JSON_UNQUOTE(json_extract(blueteam,'$.status'))", 3);//未侦测
        result.put("CountOfNotDetected", testCaseMapper.selectCount(queryWrapper));
        queryWrapper.clear();
        queryWrapper.in("campaign_id", campaignIds);
        queryWrapper.eq("JSON_UNQUOTE(json_extract(redteam,'$.status'))", 0);//TBD
        result.put("CountOfTBD", testCaseMapper.selectCount(queryWrapper));
        return result;
    }

    /**
     * 获取蓝方工具统计数据
     *
     * @param campaignIds
     * @return List
     */
    @Override
    public List<Map<String, Object>> getBlueToolData(List<String> campaignIds) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("JSON_UNQUOTE(json_extract(blueteam,'$.blueTools')) as blueTools");
        queryWrapper.in("campaign_id", campaignIds);
        List<Map<String, Object>> tools = testCaseMapper.selectMaps(queryWrapper);
        Map<String, Integer> toolCount = new HashMap<>();
        for (Map map : tools) {
            List<String> toolIds = JsonUtils.toList((String) map.get("blueTools"), String.class);
            if (toolIds == null)
                continue;
            for (String id : toolIds) {
                if (toolCount.containsKey(id)) {
                    int count = toolCount.get(id);
                    toolCount.put(id, count + 1);
                } else {
                    toolCount.put(id, 1);
                }
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (String key : toolCount.keySet()) {
            Map<String, Object> map = new HashMap<>();
            Tool tool = toolMapper.selectById(key);
            if (tool != null) {
                map.put("name",tool.getToolName());
                map.put("value",toolCount.get(key));
                result.add(map);
            }
        }
        result.sort(Comparator.comparingInt(o -> (Integer) o.get("value")));
        return result;
    }

    /**
     * 根据不同行动返回成功率
     *
     * @param campaignIds
     * @return
     */
    @Override
    public List<Map<String, Object>> getCampaignRates(List<String> campaignIds) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (String campaignId : campaignIds) {
            Map<String, Object> one = new HashMap<>();
            Campaign campaign = campaignMapper.selectById(campaignId);
            Assessment assessment = assessmentMapper.selectById(campaign.getAssessmentId());
            one.put("AssessmentName", assessment.getAssessName());
            one.put("AssessmentId", assessment.getId());
            one.put("CampaignName", campaign.getCampaignName());
            one.put("CampaignId", campaign.getId());
            one.put("score", (double) campaign.getCountOfSuccess() / (double) campaign.getTestcaseIds().size());
            results.add(one);
        }
        return results;
    }

    /**
     * 根据阶段返回成功率
     *
     * @param campaignIds
     * @return
     */
    @Override
    public List<Map<String, Object>> getPhasesRates(List<String> campaignIds) {
        List<Map<String, Object>> results = new ArrayList<>();
        List<Phase> phases = phaseMapper.selectList(null);
        for (Phase phase : phases) {
            Map<String, Object> one = new HashMap<>();
            one.put("PhaseId", phase.getId());
            one.put("PhaseName", phase.getName());
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("json_extract(redteam,'$.phase')", phase.getId());
            queryWrapper.in("campaign_id", campaignIds);
            long countOfSuccess = testCaseMapper.getCountOfSuccessByPhase(phase.getId(), campaignIds);
            long countOfTestcase = testCaseMapper.selectCount(queryWrapper);
            if (countOfTestcase==0)
                one.put("score",0);
            else
                one.put("score", (double) countOfSuccess / (double) countOfTestcase);
            results.add(one);
        }
        results.sort(((o1, o2) -> {
            if (Double.parseDouble(o2.get("score").toString()) > Double.parseDouble(o1.get("score").toString()))
                return 1;
            else if (Double.valueOf(o2.get("score").toString()).equals(Double.valueOf(o1.get("score").toString()))) {
                return 0;
            } else
                return -1;
        }));
        return results;
    }

    /**
     * 根据技术返回成功率
     *
     * @param campaignIds
     * @return
     */
    @Override
    public List<Map<String, Object>> getTechniquesRates(List<String> campaignIds) {
        List<Map<String, Object>> results = new ArrayList<>();
        List<Technique> techniques = techniqueMapper.selectList(null);
        for (Technique technique : techniques) {
            Map<String, Object> one = new HashMap<>();
            one.put("TechniqueId", technique.getId());
            one.put("TechniqueName", technique.getLabel());
            one.put("MetricId", technique.getCategory());
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("json_extract(redteam,'$.technique')", technique.getId());
            queryWrapper.in("campaign_id", campaignIds);
            long countOfSuccess = testCaseMapper.getCountOfSuccessByTechnique(technique.getId(), campaignIds);
            long countOfTestcase = testCaseMapper.selectCount(queryWrapper);
            if (countOfTestcase==0)
                one.put("score",0);
            else
                one.put("score", (double) countOfSuccess / (double) countOfTestcase);
            results.add(one);
        }
        results.sort(((o1, o2) -> {
            if (Double.parseDouble(o2.get("score").toString()) > Double.parseDouble(o1.get("score").toString()))
                return 1;
            else if (Double.valueOf(o2.get("score").toString()).equals(Double.valueOf(o1.get("score").toString())))
                return 0;
            else
                return -1;
        }));
        return results;
    }

    /**
     * 根据时间粒度返回数组
     *
     * @param campaignIds
     * @param granularity
     * @return
     */
    @Override
    public List<Map<String, Object>> getRatesByTime(List<String> campaignIds, String granularity, Date endTime) {
        List<Map<String, Object>> results = null;
        SimpleDateFormat simpleDateFormat;
        switch (granularity) {
            case "周" -> {
                Calendar beginTime = Calendar.getInstance();
                beginTime.setTimeInMillis(endTime.getTime());
                beginTime.add(Calendar.WEEK_OF_YEAR, -9);
                results = testCaseMapper.getWeeklyCount(campaignIds, new Date(beginTime.getTimeInMillis()), endTime);
                simpleDateFormat = new SimpleDateFormat("yyyy年");
                if (results.isEmpty()) {//没有结果
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endTime.getTime());
                    while (results.size() < 10) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("btime", calendar.get(Calendar.YEAR) +
                                String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)).length() == 1 ?
                                String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)) : "0" + calendar.get(Calendar.WEEK_OF_YEAR));
                        map.put("num", 0);
                        map.put("dtime", simpleDateFormat.format(calendar.getTime()) + "第" + calendar.get(Calendar.WEEK_OF_YEAR) + "周");
                        results.add(map);
                        calendar.add(Calendar.DATE, -7);
                    }
                } else if (results.size() < 10) {//结果不够10个
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endTime.getTime());
                    List<Map<String, Object>> newOne = new ArrayList<>();
                    int index = 0;
                    while (newOne.size() < 10) {
                        Map<String, Object> map = new HashMap<>();
                        String btime = String.valueOf(calendar.get(Calendar.YEAR));
                        String week = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
                        int tag = 0;
                        if (index < results.size()) {
                            String sqlBtime = (results.get(index).get("btime")).toString().substring(0, 4);
                            String sqlWeek = String.valueOf(results.get(index).get("btime")).substring(sqlBtime.length());
                            if (Integer.parseInt(sqlBtime) < Integer.parseInt(btime))
                                tag = 1;
                            else if (Integer.parseInt(sqlBtime) == Integer.parseInt(btime) &&
                                    Integer.parseInt(sqlWeek) < Integer.parseInt(week))
                                tag = 1;
                            else if (Integer.parseInt(sqlBtime) == Integer.parseInt(btime) &&
                                    Integer.parseInt(sqlWeek) == Integer.parseInt(week))
                                tag = 2;
                        } else
                            tag = 1;
                        if (index < results.size() && tag == 1) {
                            btime = btime + week;
                            map.put("btime", btime);
                            map.put("num", 0);
                            map.put("dtime", simpleDateFormat.format(calendar.getTime()) + "第" + calendar.get(Calendar.WEEK_OF_YEAR) + "周");
                            newOne.add(map);
                        } else if (index < results.size() && tag == 2) {
                            Map temp = results.get(index);
                            map.put("bitme", temp.get("btime").toString());
                            map.put("dtime", simpleDateFormat.format(calendar.getTime()) + "第" + calendar.get(Calendar.WEEK_OF_YEAR) + "周");
                            map.put("num", temp.get("num"));
                            newOne.add(map);
                            index++;
                        } else if (index >= results.size()) {
                            btime = btime + week;
                            map.put("btime", btime);
                            map.put("num", 0);
                            map.put("dtime", simpleDateFormat.format(calendar.getTime()) + "第" + calendar.get(Calendar.WEEK_OF_YEAR) + "周");
                            newOne.add(map);
                        }
                        calendar.add(Calendar.DATE, -7);
                    }
                    results = newOne;
                }
            }
            case "月" -> {
                Calendar beginTime = Calendar.getInstance();
                beginTime.setTimeInMillis(endTime.getTime());
                beginTime.add(Calendar.MONTH, -6);
                results = testCaseMapper.getMonthlyCount(campaignIds, new Date(beginTime.getTimeInMillis()), endTime);
                SimpleDateFormat btimeFormat = new SimpleDateFormat("yyyy-MM");
                if (results.isEmpty()) {//没有结果
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endTime.getTime());
                    while (results.size() < 7) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("btime", btimeFormat.format(calendar.getTime()));
                        map.put("num", 0);
                        map.put("dtime", btimeFormat.format(calendar.getTime()));
                        results.add(map);
                        calendar.add(Calendar.MONTH, -1);
                    }
                } else if (results.size() < 7) {//结果不够7个
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endTime.getTime());
                    List<Map<String, Object>> newOne = new ArrayList<>();
                    int index = 0;
                    while (newOne.size() < 7) {
                        Map<String, Object> map = new HashMap<>();
                        String btime = btimeFormat.format(calendar.getTime());
                        if (index < results.size() &&
                                ((String) results.get(index).get("btime")).compareTo(btime) < 0) {
                            map.put("btime", btime);
                            map.put("num", 0);
                            map.put("dtime", btimeFormat.format(calendar.getTime()));
                            newOne.add(map);
                        } else if (index < results.size() &&
                                ((String) results.get(index).get("btime")).compareTo(btime) == 0) {
                            Map temp = results.get(index);
                            map.put("btime", temp.get("btime").toString());
                            map.put("num", temp.get("num"));
                            map.put("dtime", btimeFormat.format(calendar.getTime()));
                            newOne.add(map);
                            index++;
                        } else if (index >= results.size()) {
                            map.put("btime", btime);
                            map.put("num", 0);
                            map.put("dtime", btimeFormat.format(calendar.getTime()));
                            newOne.add(map);
                        }
                        calendar.add(Calendar.MONTH, -1);
                    }
                    results = newOne;
                }
            }
            case "季度" -> {
                Calendar beginTime = Calendar.getInstance();
                beginTime.setTimeInMillis(endTime.getTime());
                beginTime.add(Calendar.MONTH, -14);
                results = testCaseMapper.getQuarterlyCount(campaignIds, new Date(beginTime.getTimeInMillis()), endTime);
                simpleDateFormat = new SimpleDateFormat("yyyy");
                if (results.isEmpty()) {//没有结果
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endTime.getTime());
                    while (results.size() < 5) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("btime", calendar.get(Calendar.MONTH) / 3 + 1);
                        map.put("num", 0);
                        map.put("byear", calendar.get(Calendar.YEAR));
                        map.put("dtime", simpleDateFormat.format(calendar.getTime()) + "第" + (calendar.get(Calendar.MONTH) / 3 + 1) + "季度");
                        results.add(map);
                        calendar.add(Calendar.MONTH, -3);
                    }
                } else if (results.size() < 5) {//结果不够10个
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endTime.getTime());
                    List<Map<String, Object>> newOne = new ArrayList<>();
                    int index = 0;
                    while (newOne.size() < 5) {
                        Map<String, Object> map = new HashMap<>();
                        String btime = calendar.get(Calendar.YEAR) + String.valueOf(calendar.get(Calendar.MONTH) / 3 + 1);
                        if (index < results.size() &&
                                (results.get(index).get("byear") + String.valueOf(results.get(index).get("btime"))).compareTo(btime) < 0) {
                            map.put("btime", btime);
                            map.put("num", 0);
                            map.put("byear", calendar.get(Calendar.YEAR));
                            map.put("dtime", simpleDateFormat.format(calendar.getTime()) + "第" + (calendar.get(Calendar.MONTH) / 3 + 1) + "季度");
                            newOne.add(map);
                        } else if (index < results.size() &&
                                (results.get(index).get("byear") + String.valueOf(results.get(index).get("btime"))).compareTo(btime) == 0) {
                            Map temp = results.get(index);
                            map.put("btime", temp.get("btime"));
                            map.put("num", temp.get("num"));
                            map.put("byear", calendar.get(Calendar.YEAR));
                            map.put("dtime", simpleDateFormat.format(calendar.getTime()) + "第" + (calendar.get(Calendar.MONTH) / 3 + 1) + "季度");
                            newOne.add(map);
                            index++;
                        } else if (index >= results.size()) {
                            map.put("btime", btime);
                            map.put("num", 0);
                            map.put("byear", calendar.get(Calendar.YEAR));
                            map.put("dtime", simpleDateFormat.format(calendar.getTime()) + "第" + (calendar.get(Calendar.MONTH) / 3 + 1) + "季度");
                            newOne.add(map);
                        }
                        calendar.add(Calendar.MONTH, -3);
                    }
                    results = newOne;
                }
            }
            case "年" -> {
                Calendar beginTime = Calendar.getInstance();
                beginTime.setTimeInMillis(endTime.getTime());
                beginTime.add(Calendar.YEAR, -4);
                results = testCaseMapper.getYearlyCount(campaignIds, new Date(beginTime.getTimeInMillis()), endTime);
                SimpleDateFormat btimeFormat = new SimpleDateFormat("yyyy");
                if (results.isEmpty()) {//没有结果
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endTime.getTime());
                    while (results.size() < 5) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("btime", btimeFormat.format(calendar.getTime()));
                        map.put("num", 0);
                        map.put("dtime", btimeFormat.format(calendar.getTime()) + "年");
                        results.add(map);
                        calendar.add(Calendar.YEAR, -1);
                    }
                } else if (results.size() < 5) {//结果不够5个
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endTime.getTime());
                    List<Map<String, Object>> newOne = new ArrayList<>();
                    int index = 0;
                    while (newOne.size() < 5) {
                        Map<String, Object> map = new HashMap<>();
                        String btime = btimeFormat.format(calendar.getTime());
                        if (index < results.size() &&
                                ((String) results.get(index).get("btime")).compareTo(btime) < 0) {
                            map.put("btime", btime);
                            map.put("num", 0);
                            map.put("dtime", btimeFormat.format(calendar.getTime()) + "年");
                            newOne.add(map);
                        } else if (index < results.size() &&
                                ((String) results.get(index).get("btime")).compareTo(btime) == 0) {
                            Map temp = results.get(index);
                            map.put("btime", temp.get("btime"));
                            map.put("num", temp.get("num"));
                            map.put("dtime", temp.get("btime") + "年");
                            newOne.add(map);
                            index++;
                        } else if (index >= results.size()) {
                            map.put("btime", btime);
                            map.put("num", 0);
                            map.put("dtime", btimeFormat.format(calendar.getTime()) + "年");
                            newOne.add(map);
                        }
                        calendar.add(Calendar.YEAR, -1);
                    }
                    results = newOne;
                }
            }
        }
        List<Map<String,Object>> reverse = new ArrayList<>();
        assert results != null;
        for (int i = results.size()-1; i>=0; i--)
            reverse.add(results.get(i));
        return reverse;
    }

    /**
     * 返回两个列表，最高成功率和最低成功率
     *
     * @param campaignIds
     * @return
     */
    @Override
    public Map<String, Object> getEffectiveLayers(List<String> campaignIds) {
        QueryWrapper queryWrapper = new QueryWrapper<TestCase>();
        queryWrapper.select("JSON_UNQUOTE(json_extract(blueteam,'$.detectionLayerIds')) as layerIds");
        queryWrapper.in("campaign_id", campaignIds);
        List<Map<String, Object>> layerIds = testCaseMapper.selectMaps(queryWrapper);
        queryWrapper.eq("JSON_UNQUOTE(json_extract(blueteam,'$.outcome'))", 1);
        queryWrapper.or();
        queryWrapper.eq("JSON_UNQUOTE(json_extract(blueteam,'$.outcome'))", 2);
        List<Map<String, Object>> finishedLayerIds = testCaseMapper.selectMaps(queryWrapper);
        class LayerCount {
            String layerName;
            int finishedCount;
            int totalCount;
        }
        Map<String, LayerCount> layerCountMap = new HashMap<>();
        for (Map map : layerIds) {
            List<String> layerId = JsonUtils.toList((String) map.get("layerIds"), String.class);
            if (layerId == null)
                continue;
            for (String id : layerId) {
                if (layerCountMap.containsKey(id)) {
                    int count = layerCountMap.get(id).totalCount;
                    layerCountMap.get(id).totalCount = count + 1;
                } else {
                    LayerCount layerCount = new LayerCount();
                    layerCount.totalCount = 1;
                    layerCount.finishedCount = 0;
                    layerCountMap.put(id, layerCount);
                }
            }
        }
        for (Map map : finishedLayerIds) {
            List<String> layerId = JsonUtils.toList((String) map.get("layerIds"), String.class);
            if (layerId == null)
                continue;
            for (String id : layerId) {
                int count = layerCountMap.get(id).finishedCount;
                layerCountMap.get(id).finishedCount = count + 1;
            }
        }
        List<LayerCount> layerCountList = new ArrayList<>();
        for (String id : layerCountMap.keySet()) {
            DetectionLayer layer = layerMapper.selectById(id);
            if (layer != null) {
                layerCountMap.get(id).layerName = layer.getLayerName();
                layerCountList.add(layerCountMap.get(id));
            }
        }
        layerCountList.sort((o1, o2) -> {
            double score1 = (double) o1.finishedCount / (double) o1.totalCount;
            double score2 = (double) o2.finishedCount / (double) o2.totalCount;
            if (score1 == score2)
                return o1.totalCount - o2.totalCount;
            else
                return score2 > score1 ? 1 : -1;
        });
        List<Map<String, Object>> upResults = new ArrayList<>();
        List<Map<String, Object>> downResults = new ArrayList<>();
        int index = layerCountList.size() - 1;
        if (layerCountList.size()>3) {
            for (int i = 0; i < 3; i++) {
                LayerCount layerCount = layerCountList.get(i);
                Map<String, Object> result = new HashMap<>();
                result.put("layerName", layerCount.layerName);
                result.put("totalCount", layerCount.totalCount);
                result.put("finishedCount", layerCount.finishedCount);
                result.put("percentage", (double) layerCount.finishedCount / (double) layerCount.totalCount);
                upResults.add(result);
            }
            for (int i = 0; i < 3; i++) {
                LayerCount layerCount = layerCountList.get(index - i);
                Map<String, Object> result = new HashMap<>();
                result.put("layerName", layerCount.layerName);
                result.put("totalCount", layerCount.totalCount);
                result.put("finishedCount", layerCount.finishedCount);
                result.put("percentage", (double) layerCount.finishedCount / (double) layerCount.totalCount);
                downResults.add(result);
            }
        }else {
            int max = layerCountList.size();
            for (int i = 0; i < max; i++) {
                LayerCount layerCount = layerCountList.get(i);
                Map<String, Object> result = new HashMap<>();
                result.put("layerName", layerCount.layerName);
                result.put("totalCount", layerCount.totalCount);
                result.put("finishedCount", layerCount.finishedCount);
                result.put("percentage", (double) layerCount.finishedCount / (double) layerCount.totalCount);
                upResults.add(result);
            }
            for (int i = 0; i < max; i++) {
                LayerCount layerCount = layerCountList.get(index - i);
                Map<String, Object> result = new HashMap<>();
                result.put("layerName", layerCount.layerName);
                result.put("totalCount", layerCount.totalCount);
                result.put("finishedCount", layerCount.finishedCount);
                result.put("percentage", (double) layerCount.finishedCount / (double) layerCount.totalCount);
                downResults.add(result);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("up", upResults);
        result.put("down", downResults);
        return result;
    }

    /**
     * 返回前十个使用最多的防守工具
     *
     * @param campaignIds
     * @return
     */
    @Override
    public List<Map<String, Object>> getTop10BlueTool(List<String> campaignIds) {
        List<Map<String, Object>> data = getBlueToolData(campaignIds);
        if (data.size()<=10)
            return data;
        return getBlueToolData(campaignIds).subList(data.size()-10, data.size());
    }

    @Override
    public List<Map<String, Object>> getPhaseStatistics(List<String> campaignIds) {
        class PhaseCount {
            final String phaseName;
            int TBDCount;
            int blockedCount;
            int detectedCount;
            int unDetectedCount;

            PhaseCount(String phaseName) {
                this.phaseName = phaseName;
                TBDCount = 0;
                blockedCount = 0;
                detectedCount = 0;
                unDetectedCount = 0;
            }
        }
        List<Phase> phaseList = phaseMapper.selectList(null);
        Map<String, PhaseCount> phaseCounts = new HashMap<>();
        for (Phase phase : phaseList) {
            if (phaseCounts.containsKey(phase))
                continue;
            else {
                PhaseCount phaseCount = new PhaseCount(phase.getName());
                phaseCounts.put(phase.getId(), phaseCount);
            }
        }
        List<Map<String, Object>> TBDList = testCaseMapper.getPhaseStatic(campaignIds, 0);
        List<Map<String, Object>> blockedList = testCaseMapper.getPhaseStatic(campaignIds, 1);
        List<Map<String, Object>> detectedList = testCaseMapper.getPhaseStatic(campaignIds, 2);
        List<Map<String, Object>> unDetectedList = testCaseMapper.getPhaseStatic(campaignIds, 3);
        for (Map<String, Object> map : TBDList) {
            String id = (String) map.get("phaseId");
            PhaseCount phaseCount = phaseCounts.get(id);
            phaseCount.TBDCount = ((Number) map.get("COUNT(*)")).intValue();
            phaseCounts.put(id, phaseCount);
        }
        for (Map<String, Object> map : blockedList) {
            String id = (String) map.get("phaseId");
            PhaseCount phaseCount = phaseCounts.get(id);
            phaseCount.blockedCount = ((Number) map.get("COUNT(*)")).intValue();
            phaseCounts.put(id, phaseCount);
        }
        for (Map<String, Object> map : detectedList) {
            String id = (String) map.get("phaseId");
            PhaseCount phaseCount = phaseCounts.get(id);
            phaseCount.detectedCount = ((Number) map.get("COUNT(*)")).intValue();
            phaseCounts.put(id, phaseCount);
        }
        for (Map<String, Object> map : unDetectedList) {
            String id = (String) map.get("phaseId");
            PhaseCount phaseCount = phaseCounts.get(id);
            phaseCount.unDetectedCount = ((Number) map.get("COUNT(*)")).intValue();
            phaseCounts.put(id, phaseCount);
        }
        List<Map<String, Object>> results = new ArrayList<>();
        for (PhaseCount phaseCount : phaseCounts.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("phaseName", phaseCount.phaseName);
            map.put("TBDCount", phaseCount.TBDCount);
            map.put("blockedCount", phaseCount.blockedCount);
            map.put("detectedCount", phaseCount.detectedCount);
            map.put("unDetectedCount", phaseCount.unDetectedCount);
            results.add(map);
        }
        return results;
    }
}
