package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littlepants.attack.attackweb.entity.*;
import com.littlepants.attack.attackweb.mapper.*;
import com.littlepants.attack.attackweb.service.intf.TestCaseTempService;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Service
public class TestCaseTempServiceImpl implements TestCaseTempService {
    private final TestCaseTemplateMapper testCaseTemplateMapper;
    private final TechniqueMapper techniqueMapper;
    private final TestCaseMapper testCaseMapper;
    private final PhaseMapper phaseMapper;
    private final ToolMapper toolMapper;
    private final LayerMapper layerMapper;

    public TestCaseTempServiceImpl(TestCaseTemplateMapper testCaseTemplateMapper,
                                   TechniqueMapper techniqueMapper, TestCaseMapper testCaseMapper,
                                   PhaseMapper phaseMapper, ToolMapper toolMapper, LayerMapper layerMapper) {
        this.testCaseTemplateMapper = testCaseTemplateMapper;
        this.techniqueMapper = techniqueMapper;
        this.testCaseMapper = testCaseMapper;
        this.phaseMapper = phaseMapper;
        this.toolMapper = toolMapper;
        this.layerMapper = layerMapper;
    }

    private void resetTemplate(TestCaseTemplate testCaseTemplate) {
        testCaseTemplate.setTeamIds(null);
        testCaseTemplate.setEvidenceFiles(null);
        resetTeamInfo(testCaseTemplate.getBlueTeam(), testCaseTemplate.getRedTeam());
    }

    private void resetTeamInfo(BlueTeam blueTeam2, RedTeam redTeam2) {
        blueTeam2.setOutcome("0");
        blueTeam2.setOutcomeNote(null);
        blueTeam2.setDetectionTime(null);
        blueTeam2.setAlertSeverity("0");
        blueTeam2.setAlertTriggered("0");
        blueTeam2.setActivityLogged("0");
        redTeam2.setStartTime(null);
        redTeam2.setEndTime(null);
        redTeam2.setStatus("0");
        redTeam2.setTargetAssets(null);
        redTeam2.setSourceIPs(null);
    }

    @Override
    public int addTemplate(TestCaseTemplate testCaseTemplate) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        testCaseTemplate.setCreateTime(timestamp);
        testCaseTemplate.setUpdateTime(timestamp);
        testCaseTemplate.setId(UUIDGenerator.generateUUID());
        resetTemplate(testCaseTemplate);
        return testCaseTemplateMapper.insert(testCaseTemplate);
    }

    @Override
    public int updateTemplate(TestCaseTemplate testCaseTemplate) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        testCaseTemplate.setUpdateTime(timestamp);
        resetTemplate(testCaseTemplate);
        return testCaseTemplateMapper.updateById(testCaseTemplate);
    }

    @Override
    public int deleteTemplateById(String id) {
        return testCaseTemplateMapper.deleteById(id);
    }

    @Override
    public int cloneTemplateById(String id, String templateName) {
        TestCaseTemplate testCaseTemplate = testCaseTemplateMapper.selectById(id);
        testCaseTemplate.setId(UUIDGenerator.generateUUID());
        testCaseTemplate.setTemplateName(templateName);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        testCaseTemplate.setCreateTime(timestamp);
        testCaseTemplate.setUpdateTime(timestamp);
        return testCaseTemplateMapper.insert(testCaseTemplate);
    }

    @Override
    public int createTemplateFromTestCase(String id, String templateName) {
        TestCase testCase = testCaseMapper.selectById(id);
        TestCaseTemplate testCaseTemplate = new TestCaseTemplate();
        testCaseTemplate.setId(UUIDGenerator.generateUUID());
        Timestamp timestamp = new Timestamp(new Date().getTime());
        testCaseTemplate.setCreateTime(timestamp);
        testCaseTemplate.setUpdateTime(timestamp);
        testCaseTemplate.setTemplateName(templateName);
        resetTeamInfo(testCase.getBlueTeam(), testCase.getRedTeam());
        testCaseTemplate.setRedTeam(testCase.getRedTeam());
        testCaseTemplate.setBlueTeam(testCase.getBlueTeam());
        return testCaseTemplateMapper.insert(testCaseTemplate);
    }

    @Override
    public TestCaseTemplate getTemplateById(String id) {
        TestCaseTemplate testCaseTemplate = testCaseTemplateMapper.selectById(id);
        RedTeam redTeam = testCaseTemplate.getRedTeam();
        List<String> tools = redTeam.getRedTools();
        if (tools==null||tools.isEmpty())
            redTeam.setRedToolInfo(new ArrayList<>());
        else {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("id","tool_name","vendor_name");
            queryWrapper.in("id",tools);
            redTeam.setRedToolInfo(toolMapper.selectMaps(queryWrapper));
        }
        BlueTeam blueTeam = testCaseTemplate.getBlueTeam();
        List<String> layers = blueTeam.getDetectionLayerIds();
        if (layers==null||layers.isEmpty())
            blueTeam.setLayerNames(new ArrayList<>());
        else {
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id",layers);
            List<DetectionLayer> list = layerMapper.selectList(queryWrapper);
            List<String> names = new ArrayList<>();
            for (DetectionLayer detectionLayer:list)
                names.add(detectionLayer.getLayerName());
            blueTeam.setLayerNames(names);
        }
        return testCaseTemplate;
    }

    @Override
    public List<Map<String, Object>> getAllTemplate() {
        //返回某些字段的合集
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","template_name",
                "JSON_UNQUOTE(json_extract(redteam,'$.phase')) as phase",
                "JSON_UNQUOTE(json_extract(redteam,'$.technique')) as technique");
        List<Map<String,Object>> templates = testCaseTemplateMapper.selectMaps(queryWrapper);
        for (Map val:templates){
            if (val.get("phase")!=null)
                val.put("phase",phaseMapper.selectById((Serializable) val.get("phase")).getName());
            if (val.get("technique")!=null&&techniqueMapper.selectById((Serializable) val.get("technique"))!=null)
                val.put("technique",techniqueMapper.selectById((Serializable) val.get("technique")).getLabel());
        }
        return templates;
    }

    @Override
    public Map<String, Object> getTemplatesByPage(int page, int count) {
        Page<Map<String,Object>> pages = new Page<>(page,count);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","template_name",
                "JSON_UNQUOTE(json_extract(redteam,'$.phase')) as phase",
                "JSON_UNQUOTE(json_extract(redteam,'$.technique')) as technique");
        Page<Map<String,Object>> result = testCaseTemplateMapper.selectMapsPage(pages,queryWrapper);
        List<Map<String,Object>> templates = result.getRecords();
        for (Map val:templates){
            if (val.get("phase")!=null)
                val.put("phase",phaseMapper.selectById((Serializable) val.get("phase")).getName());
            if (val.get("technique")!=null&&techniqueMapper.selectById((Serializable) val.get("technique"))!=null)
                val.put("technique",techniqueMapper.selectById((Serializable) val.get("technique")).getLabel());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("data",templates);
        map.put("count",result.getPages());
        return map;
    }

}
