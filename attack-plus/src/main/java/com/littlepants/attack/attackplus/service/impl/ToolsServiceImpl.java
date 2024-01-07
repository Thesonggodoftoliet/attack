package com.littlepants.attack.attackplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.dao.ToolsDao;
import com.littlepants.attack.attackplus.entity.Tools;
import com.littlepants.attack.attackplus.service.ToolsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Service
public class ToolsServiceImpl extends ServiceImpl<ToolsDao, Tools> implements ToolsService {
    private final ToolsDao toolsDao;

    public ToolsServiceImpl(ToolsDao toolsDao) {
        this.toolsDao = toolsDao;
    }

    @Override
    public List<Map<String, Object>> getVendors() {
        return toolsDao.getVendors();
    }

    @Override
    public List<Tools> getToolsByVendor(String vendorName) {
        return toolsDao.getToolsByVendor(vendorName);
    }
}
