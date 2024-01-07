package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.littlepants.attack.attackweb.entity.*;
import com.littlepants.attack.attackweb.mapper.*;
import com.littlepants.attack.attackweb.service.intf.TestcaseService;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TestcaseServiceImpl implements TestcaseService {
    private final TestCaseMapper testCaseMapper;
    private final TestCaseTemplateMapper testCaseTemplateMapper;
    private final CampaignMapper campaignMapper;
    private final TimeLineMapper timeLineMapper;
    private final ToolMapper toolMapper;
    private final LayerMapper layerMapper;

    public TestcaseServiceImpl(TestCaseMapper testCaseMapper, TestCaseTemplateMapper testCaseTemplateMapper,
                               CampaignMapper campaignMapper, TimeLineMapper timeLineMapper, ToolMapper toolMapper,
                               LayerMapper layerMapper) {
        this.testCaseMapper = testCaseMapper;
        this.testCaseTemplateMapper = testCaseTemplateMapper;
        this.campaignMapper = campaignMapper;
        this.timeLineMapper = timeLineMapper;
        this.toolMapper = toolMapper;
        this.layerMapper = layerMapper;
    }

    @Override
    @Transactional
    public int addTestCase(TestCase testCase) {
        String id = UUIDGenerator.generateUUID();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        testCase.setId(id);
        testCase.setCreateTime(timestamp);
        testCase.setUpdateTime(timestamp);
        testCase.getRedTeam().setStatus("0");
        testCase.getBlueTeam().setOutcome("0");
        Campaign campaign = campaignMapper.selectById(testCase.getCampaignId());
        List<String> testcaseIds = campaign.getTestcaseIds();
        testcaseIds.add(id);
        try {
            campaignMapper.updateById(campaign);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        TimeLine timeLine = new TimeLine();
        timeLine.setCreateTime(timestamp);
        timeLine.setTestcaseId(testCase.getId());
        timeLine.setCampaignId(testCase.getCampaignId());
        timeLine.setTestcaseName(testCase.getTestcaseName());
        timeLine.setDescription("被添加");
        try {
            timeLineMapper.insert(timeLine);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        return testCaseMapper.insert(testCase);
    }

    @Override
    @Transactional
    public String addTestCaseFromTemplate(String testcaseTempId, String campaignId) {
        TestCaseTemplate testCaseTemplate = testCaseTemplateMapper.selectById(testcaseTempId);
        TestCase testCase = new TestCase();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        testCase.setUpdateTime(timestamp);
        testCase.setCreateTime(timestamp);
        testCase.setId(UUIDGenerator.generateUUID());
        testCase.setBlueTeam(testCaseTemplate.getBlueTeam());
        testCase.setCampaignId(campaignId);
        testCase.setRedTeam(testCaseTemplate.getRedTeam());
        testCase.setTestcaseName(testCaseTemplate.getTemplateName());
        testCase.getRedTeam().setStatus("0");
        testCase.getBlueTeam().setOutcome("0");
        TimeLine timeLine = new TimeLine();
        timeLine.setCreateTime(timestamp);
        timeLine.setTestcaseId(testCase.getId());
        timeLine.setTestcaseName(testCase.getTestcaseName());
        timeLine.setCampaignId(testCase.getCampaignId());
        timeLine.setDescription("被添加");
        try {
            timeLineMapper.insert(timeLine);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        int tag = testCaseMapper.insert(testCase);
        if (tag == 0)
            return null;
        else
            return testCase.getId();
    }

    @Override
    public TestCase getTestCaseById(String id) {
        TestCase testCase = testCaseMapper.selectById(id);
        RedTeam redTeam = testCase.getRedTeam();
        List<String> tools = redTeam.getRedTools();
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","tool_name","vendor_name");
        queryWrapper.in("id",tools);
        if (tools==null||tools.isEmpty())
            redTeam.setRedToolInfo(new ArrayList<>());
        else
            redTeam.setRedToolInfo(toolMapper.selectMaps(queryWrapper));
        BlueTeam blueTeam = testCase.getBlueTeam();
        List<String> layers = blueTeam.getDetectionLayerIds();
        if (layers==null||layers.isEmpty())
            blueTeam.setLayerNames(new ArrayList<>());
        else{
            queryWrapper.clear();
            queryWrapper.in("id",layers);
            List<DetectionLayer> results = layerMapper.selectList(queryWrapper);
            List<String> strings = new ArrayList<>();
            for (DetectionLayer detectionLayer:results)
                strings.add(detectionLayer.getLayerName());
            blueTeam.setLayerNames(strings);
        }
        return testCase;
    }

    @Override
    @Transactional
    public int updateTestCaseById(TestCase testCase) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        testCase.setUpdateTime(timestamp);
        TestCase sqlTestcase = testCaseMapper.selectById(testCase.getId());
        TimeLine timeLine = new TimeLine();
        timeLine.setTestcaseId(testCase.getId());
        timeLine.setCampaignId(sqlTestcase.getCampaignId());
        timeLine.setTestcaseName(testCase.getTestcaseName());
        if ((sqlTestcase.getRedTeam().getStatus()==null&&testCase.getRedTeam().getStatus()!=null)
                ||(sqlTestcase.getRedTeam().getStatus()!=null&&
                !sqlTestcase.getRedTeam().getStatus().equals(testCase.getRedTeam().getStatus()))){
            String str = switch (Integer.parseInt(testCase.getRedTeam().getStatus())) {
                case 1 -> "进行中";
                case 2 -> "暂停";
                case 3 -> "已完成";
                case 4 -> "已废弃";
                default -> null;
            };
            if (Integer.parseInt(testCase.getRedTeam().getStatus())==3){
                int totalCount = testCaseMapper.getCountOfSameCampaign(sqlTestcase.getCampaignId());
                int countOfFinished = testCaseMapper.getCountOfFinished(sqlTestcase.getCampaignId());
                double progress = (double) countOfFinished+1/(double) totalCount;
                UpdateWrapper updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("progress", progress*100);
                updateWrapper.eq("id",sqlTestcase.getCampaignId());
                campaignMapper.update(null,updateWrapper);
            }
            timeLine.setId(UUIDGenerator.generateUUID());
            timeLine.setDescription("攻击状态更改为"+str);
            timestamp.setTime(new Date().getTime());
            timeLine.setCreateTime(timestamp);
            try {
                timeLineMapper.insert(timeLine);
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        if ((sqlTestcase.getBlueTeam().getOutcome()==null&&testCase.getBlueTeam().getOutcome()!=null)
                ||(sqlTestcase.getBlueTeam().getOutcome()!=null
                &&!sqlTestcase.getBlueTeam().getOutcome().equals(testCase.getBlueTeam().getOutcome()))){
            String str = switch (Integer.parseInt(testCase.getBlueTeam().getOutcome())) {
                case 0 -> "待决定";
                case 1 -> "已封锁";
                case 2 -> "已侦测";
                case 3 -> "未侦测";
                default -> null;
            };
            if (Integer.parseInt(testCase.getBlueTeam().getOutcome())!=0){
                if (Integer.parseInt(testCase.getBlueTeam().getOutcome())==1||
                        Integer.parseInt(testCase.getBlueTeam().getOutcome())==2) {
                    int totalCount = testCaseMapper.getCountOfSameCampaign(sqlTestcase.getCampaignId());
                    int CountOfTBD = testCaseMapper.getCountOfTBD(sqlTestcase.getCampaignId());
                    UpdateWrapper updateWrapper = new UpdateWrapper<>();
                    updateWrapper.set("count_of_success", totalCount-CountOfTBD+1);
                    updateWrapper.eq("id", sqlTestcase.getCampaignId());
                    campaignMapper.update(null, updateWrapper);
                }else if (Integer.parseInt(testCase.getBlueTeam().getOutcome())==3){
                    int totalCount = testCaseMapper.getCountOfSameCampaign(sqlTestcase.getCampaignId());
                    int CountOfTBD = testCaseMapper.getCountOfTBD(sqlTestcase.getCampaignId());
                    UpdateWrapper updateWrapper = new UpdateWrapper<>();
                    updateWrapper.set("count_of_fail", totalCount-CountOfTBD+1);
                    updateWrapper.eq("id", sqlTestcase.getCampaignId());
                    campaignMapper.update(null, updateWrapper);
                }
            }
            timeLine.setId(UUIDGenerator.generateUUID());
            timeLine.setDescription("防御状态更改为"+str);
            timestamp.setTime(new Date().getTime());
            timeLine.setCreateTime(timestamp);
            try {
                timeLineMapper.insert(timeLine);
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        return testCaseMapper.updateById(testCase);
    }

    @Override
    @Transactional
    public int deleteTestCaseById(String id) {
        TestCase testCase = testCaseMapper.selectById(id);
//        System.out.println(JsonUtils.toString(testCase));
        Campaign campaign = campaignMapper.selectById(testCase.getCampaignId());
        List<String> testcaseIds = campaign.getTestcaseIds();
        testcaseIds.remove(id);
        campaign.setTestcaseIds(testcaseIds);
        try {
            campaignMapper.updateById(campaign);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        TestCase sqlTestcase = testCaseMapper.selectById(testCase.getId());
        TimeLine timeLine =  new TimeLine();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        timeLine.setCreateTime(timestamp);
        timeLine.setTestcaseId(testCase.getId());
        timeLine.setCampaignId(sqlTestcase.getCampaignId());
        timeLine.setDescription("被舍弃");
        timeLine.setTestcaseName(testCase.getTestcaseName());
        try {
            timeLineMapper.insert(timeLine);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        return testCaseMapper.deleteById(id);
    }

    @Override
    public int cloneTestCaseById(String id) {
        TestCase testCase = testCaseMapper.selectById(id);
        testCase.resetData();
        Campaign campaign = campaignMapper.selectById(testCase.getCampaignId());
        List<String> testcaseIds = campaign.getTestcaseIds();
        testcaseIds.add(testCase.getId());
        campaign.setTestcaseIds(testcaseIds);
        try {
            campaignMapper.updateById(campaign);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        testCase.getRedTeam().setStatus("0");
        testCase.getBlueTeam().setOutcome("0");
        Timestamp timestamp = new Timestamp(new Date().getTime());
        TimeLine timeLine = new TimeLine();
        timeLine.setCreateTime(timestamp);
        timeLine.setTestcaseId(testCase.getId());
        timeLine.setCampaignId(testCase.getCampaignId());
        timeLine.setTestcaseName(testCase.getTestcaseName());
        timeLine.setDescription("被添加");
        try {
            timeLineMapper.insert(timeLine);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        return testCaseMapper.insert(testCase);
    }
}
