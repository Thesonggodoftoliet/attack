package com.littlepants.attack.attackplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.littlepants.attack.attackplus.entity.Tools;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
public interface ToolsService extends IService<Tools>{
    List<Map<String,Object>> getVendors();
    List<Tools>  getToolsByVendor(String vendorName);
}
