package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.littlepants.attack.attackweb.entity.Tool;
import com.littlepants.attack.attackweb.entity.Vendor;
import com.littlepants.attack.attackweb.mapper.PhaseMapper;
import com.littlepants.attack.attackweb.mapper.ToolMapper;
import com.littlepants.attack.attackweb.mapper.VendorMapper;
import com.littlepants.attack.attackweb.service.intf.ToolService;
import com.littlepants.attack.attackweb.util.JsonUtils;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ToolServiceImpl implements ToolService {
    private final ToolMapper toolMapper;
    private final VendorMapper vendorMapper;
    private final PhaseMapper phaseMapper;

    public ToolServiceImpl(ToolMapper toolMapper, VendorMapper vendorMapper, PhaseMapper phaseMapper) {
        this.toolMapper = toolMapper;
        this.vendorMapper = vendorMapper;
        this.phaseMapper = phaseMapper;
    }

    @Override
    public int addTool(Tool tool) {
        tool.setId(UUIDGenerator.generateUUID());
        Timestamp timestamp = new Timestamp(new Date().getTime());
        tool.setCreateTime(timestamp);
        tool.setUpdateTime(timestamp);
        Vendor vendor = vendorMapper.selectById(tool.getVendorName());
        tool.setVendorName(vendor.getVendorName());
        tool.setVendorId(vendor.getId());
        System.out.println(JsonUtils.toString(tool));
        return toolMapper.insert(tool);
    }

    @Override
    public int updateTool(Tool tool) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        tool.setUpdateTime(timestamp);
        System.out.println(JsonUtils.toString(tool));
        return toolMapper.updateById(tool);
    }

    @Override
    public int deleteToolById(String id) {
        return toolMapper.deleteById(id);
    }

    @Override
    public Tool getToolById(String id) {
        return toolMapper.selectById(id);
    }

    @Override
    public List<Tool> getAllTool() {
        List<Tool> toolList = toolMapper.selectList(null);
        for (Tool tool:toolList){
            List<String> ids = tool.getPhaseIds();
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id",ids);
            queryWrapper.select("phase_name");
            tool.setPhaseIds(phaseMapper.selectObjs(queryWrapper));
        }
        return toolList;
    }

    @Override
    public List<Map<String,Object>> getDifferentTools(boolean isBlue) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_blue",isBlue);
        queryWrapper.select("id","tool_name","vendor_name");
        return toolMapper.selectMaps(queryWrapper);
    }
}
