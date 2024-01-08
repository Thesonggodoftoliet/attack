package com.littlepants.attack.attackplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.littlepants.attack.attackplus.entity.Timeline;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
public interface TimelineService extends IService<Timeline>{
    void deleteBatchByOperationId(Long operationId);
    List<Timeline> getTimelines(Long operationId);
}
