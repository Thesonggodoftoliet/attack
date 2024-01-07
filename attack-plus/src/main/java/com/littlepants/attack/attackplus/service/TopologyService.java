package com.littlepants.attack.attackplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.littlepants.attack.attackplus.entity.Topology;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-05-24
 */
public interface TopologyService extends IService<Topology>{
    public Topology getByMeta2dId(String meta2dId);
    public Topology getLatest();
}
