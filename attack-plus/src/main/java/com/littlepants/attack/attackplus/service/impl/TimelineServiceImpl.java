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
    private final HostService hostService;
    private final CaseService caseService;

    public TimelineServiceImpl(TimelineDao timelineDao, HostService hostService, @Lazy CaseService caseService) {
        this.timelineDao = timelineDao;
        this.hostService = hostService;
        this.caseService = caseService;
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void deleteBatchByCaseId(Long caseId) {
        timelineDao.deleteBatchByCaseId(caseId);
    }

    /**
     * 通过caldera的能力ID与执行方案ID插入时间线
     * @param operationId Long
     * @param abilityId String
     * @param timeline Timeline
     * @param paw String
     */
    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void insetByOperationAndAbility(Long operationId, String abilityId, Timeline timeline,String paw) {
        List<Long> ids = caseService.getIdsByOperationAndAbility(operationId,abilityId);
        timeline.setCaseId(ids.get(0));
        timeline.setIp(hostService.getIpByPaw(paw));
        timelineDao.insert(timeline);
    }

    @Override
    public List<Timeline> getTimelines(Long caseId) {
        return null;
    }
}
