package com.littlepants.attack.attackplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.dao.TimelineDao;
import com.littlepants.attack.attackplus.entity.Timeline;
import com.littlepants.attack.attackplus.service.CaseService;
import com.littlepants.attack.attackplus.service.HostService;
import com.littlepants.attack.attackplus.service.TimelineService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Service
public class TimelineServiceImpl extends ServiceImpl<TimelineDao, Timeline> implements TimelineService {
    private final TimelineDao timelineDao;

    public TimelineServiceImpl(TimelineDao timelineDao) {
        this.timelineDao = timelineDao;
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void deleteBatchByOperationId(Long operationId) {
        timelineDao.deleteBatchByOperationId(operationId);
    }


    @Override
    public List<Timeline> getTimelines(Long operationId) {
        return timelineDao.getTimelines(operationId);
    }
}
