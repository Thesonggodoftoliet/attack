package com.littlepants.attack.attackweb.service.intf;

import com.littlepants.attack.attackweb.entity.Tool;

import java.util.List;
import java.util.Map;

public interface ToolService {
    int addTool(Tool tool);
    int updateTool(Tool tool);
    int deleteToolById(String id);
    Tool getToolById(String id);
    List<Tool> getAllTool();
    List<Map<String,Object>> getDifferentTools(boolean isBlue);
}
