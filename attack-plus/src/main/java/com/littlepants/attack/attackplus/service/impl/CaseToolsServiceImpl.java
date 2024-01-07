package com.littlepants.attack.attackplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.dao.CaseToolsDao;
import com.littlepants.attack.attackplus.entity.CaseTools;
import com.littlepants.attack.attackplus.service.CaseToolsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-07
 */
@Service
public class CaseToolsServiceImpl extends ServiceImpl<CaseToolsDao, CaseTools> implements CaseToolsService {
    private final CaseToolsDao caseToolsDao;

    public CaseToolsServiceImpl(CaseToolsDao caseToolsDao) {
        this.caseToolsDao = caseToolsDao;
    }

    @Override
    @Transactional(transactionManager = "transactionManager",rollbackFor = Exception.class)
    public void deleteBatchByCaseId(Long caseId) {
        caseToolsDao.deleteBatchByCaseId(caseId);
    }

    @Override
    @Transactional(transactionManager = "transactionManager",rollbackFor = Exception.class)
    public void deleteBatchByToolId(Long toolId) {
        caseToolsDao.deleteBatchByToolId(toolId);
    }
}
