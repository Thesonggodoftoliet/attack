package com.littlepants.attack.attackplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.dao.TopologyDao;
import com.littlepants.attack.attackplus.entity.Topology;
import com.littlepants.attack.attackplus.service.TopologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-05-24
 */
@Service
public class TopologyServiceImpl extends ServiceImpl<TopologyDao,Topology> implements TopologyService {
    private final TopologyDao topologyDao;

    public TopologyServiceImpl(TopologyDao topologyDao) {
        this.topologyDao = topologyDao;
    }

    @Override
    public Topology getByMeta2dId(String meta2dId) {
        QueryWrapper<Topology> wrapper = new QueryWrapper<>();
        return topologyDao.selectOne(wrapper.eq("meta2d_id",meta2dId));
    }

    @Override
    public Topology getLatest() {
        QueryWrapper<Topology> queryWrapper = new QueryWrapper<>();
        return topologyDao.selectOne(queryWrapper.orderByDesc("update_time").last("limit 1"));
    }

}
