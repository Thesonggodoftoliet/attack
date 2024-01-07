package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littlepants.attack.attackweb.entity.CampaignTemplate;
import com.littlepants.attack.attackweb.mapper.CampaignTemplateMapper;
import com.littlepants.attack.attackweb.mapper.PhaseMapper;
import com.littlepants.attack.attackweb.mapper.TechniqueMapper;
import com.littlepants.attack.attackweb.mapper.TestCaseTemplateMapper;
import com.littlepants.attack.attackweb.service.intf.CampaignTempService;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CampaignTempServiceImpl implements CampaignTempService {
    private final CampaignTemplateMapper campaignTemplateMapper;
    private final TestCaseTemplateMapper testCaseTemplateMapper;
    private final PhaseMapper phaseMapper;
    private final TechniqueMapper techniqueMapper;

    public CampaignTempServiceImpl(CampaignTemplateMapper campaignTemplateMapper,
                                   TestCaseTemplateMapper testCaseTemplateMapper, PhaseMapper phaseMapper,
                                   TechniqueMapper techniqueMapper) {
        this.campaignTemplateMapper = campaignTemplateMapper;
        this.testCaseTemplateMapper = testCaseTemplateMapper;
        this.phaseMapper = phaseMapper;
        this.techniqueMapper = techniqueMapper;
    }

    @Override
    public int addCampaignTemplate(CampaignTemplate campaignTemplate) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        campaignTemplate.setCreateTime(timestamp);
        campaignTemplate.setUpdateTime(timestamp);
        return campaignTemplateMapper.insert(campaignTemplate);
    }

    @Override
    public int updateCampaignTemplate(CampaignTemplate campaignTemplate) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        campaignTemplate.setUpdateTime(timestamp);
        return campaignTemplateMapper.updateById(campaignTemplate);
    }

    @Override
    public int deleteCampaignTemplateById(String id) {
        return campaignTemplateMapper.deleteById(id);
    }

    @Override
    public int cloneCampaignTemplateById(String id) {
        CampaignTemplate campaignTemplate = getTemplateById(id);
        CampaignTemplate clone = new CampaignTemplate();
        clone.setId(UUIDGenerator.generateUUID());
        clone.setTemplateName(campaignTemplate.getTemplateName()+"副本");
        Timestamp timestamp = new Timestamp(new Date().getTime());
        clone.setCreateTime(timestamp);
        clone.setUpdateTime(timestamp);
        clone.setTestcaseCounts(campaignTemplate.getTestcaseCounts());
        clone.setTemplateDescription(campaignTemplate.getTemplateDescription());
        clone.setTestcaseIds(campaignTemplate.getTestcaseIds());
        return campaignTemplateMapper.insert(clone);
    }

    @Override
    public CampaignTemplate getTemplateById(String id) {
        return campaignTemplateMapper.selectById(id);
    }

    @Override
    public List<CampaignTemplate> getAllTemplates() {
        List<CampaignTemplate> campaignTemplates = campaignTemplateMapper.selectList(null);
        for (CampaignTemplate campaignTemplate : campaignTemplates) {
            campaignTemplate.setTestcaseCounts(campaignTemplate.getTestcaseIds().size());
        }
        return campaignTemplates;
    }

    @Override
    public Map<String, Object> getTemplatesByPage(int page, int count) {
        Page<CampaignTemplate> templatePage = new Page<>(page,count);
        Page<CampaignTemplate> results = campaignTemplateMapper.selectPage(templatePage,null);
        List<CampaignTemplate> templates = results.getRecords();
        for (CampaignTemplate campaignTemplate:templates){
            campaignTemplate.setTestcaseCounts(campaignTemplate.getTestcaseIds().size());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("data",templates);
        map.put("count",results.getPages());
        return map;
    }

    @Override
    public int createTemplateFromJSON() {
        return 0;
    }

    @Override
    public List<Map<String, Object>> getTestcaseTempByCampaignId(String campaignId) {
        CampaignTemplate campaignTemplate = campaignTemplateMapper.selectById(campaignId);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","template_name","JSON_UNQUOTE(json_extract(redteam,'$.phase')) as phase",
                "JSON_UNQUOTE(json_extract(redteam,'$.technique')) as technique");
        queryWrapper.in("id",campaignTemplate.getTestcaseIds());
        List<Map<String,Object>> templates = testCaseTemplateMapper.selectMaps(queryWrapper);
        for (Map val:templates){
            if (val.get("phase")!=null)
                val.put("phase",phaseMapper.selectById((Serializable) val.get("phase")).getName());
            if (val.get("technique")!=null&&techniqueMapper.selectById((Serializable) val.get("technique"))!=null)
                val.put("technique",techniqueMapper.selectById((Serializable) val.get("technique")).getLabel());
        }
        return templates;
    }
}
