package com.littlepants.attack.attackplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.dao.CaseDao;
import com.littlepants.attack.attackplus.dto.CaseCalderaDTO;
import com.littlepants.attack.attackplus.dto.CaseDTO;
import com.littlepants.attack.attackplus.entity.Case;
import com.littlepants.attack.attackplus.entity.CaseTools;
import com.littlepants.attack.attackplus.entity.Host;
import com.littlepants.attack.attackplus.entity.TestcaseCaldera;
import com.littlepants.attack.attackplus.mapper.CaseMapper;
import com.littlepants.attack.attackplus.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
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
public class CaseServiceImpl extends ServiceImpl<CaseDao,Case> implements CaseService {
    private final CaseToolsService caseToolsService;
    private final TimelineService timelineService;
    private final HostService hostService;
    private final TestcaseCalderaService testcaseCalderaService;
    private final CaseDao caseDao;

    public CaseServiceImpl(CaseToolsService caseToolsService, TimelineService timelineService,
                           HostService hostService, TestcaseCalderaService testcaseCalderaService, CaseDao caseDao) {
        this.caseToolsService = caseToolsService;
        this.timelineService = timelineService;
        this.hostService = hostService;
        this.testcaseCalderaService = testcaseCalderaService;
        this.caseDao = caseDao;
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void assignTools(Long caseId, List<Long> tools) {
        List<CaseTools> caseTools = new ArrayList<>();
        for (Long id:tools)
            caseTools.add(new CaseTools(caseId,id));
        caseToolsService.saveBatch(caseTools);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void deleteById(Long caseId) {
        caseToolsService.deleteBatchByCaseId(caseId);
        caseDao.deleteById(caseId);
    }

    @Override
    public List<Long> getIdsByOperationAndAbility(Long operationId, String abilityId) {
        return caseDao.getIdsByOperationAndAbility(operationId,abilityId);
    }

    /**
     * 读取Caldera记录更新用例
     * @param cases List<CaseParamsDTO>
     */
    @Override
    @Transactional(transactionManager = "transactionManager",rollbackFor = Exception.class)
    public void updateFromCaldera(List<CaseCalderaDTO> cases) {
        for (CaseCalderaDTO dto:cases){
            Host host = hostService.getHostByPaw(dto.getPaw());
            Case mycase = CaseMapper.INSTANCE.paramDTOToCase(dto);
            QueryWrapper<TestcaseCaldera> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ability_id",dto.getAbilityId());
            TestcaseCaldera testcaseCaldera = testcaseCalderaService.getOne(queryWrapper);
            mycase.setTargetHost(host.getHostName());
            mycase.setTargetIp(host.getIp());
            mycase.setTestcaseId(testcaseCaldera.getId());
            mycase.setTestcaseName(testcaseCaldera.getTestcaseName());
            caseDao.insert(mycase);
        }
    }

    /**
     * 根据operationId删除用例
     * @param operationId Long
     */
    @Override
    @Transactional(transactionManager = "transactionManager",rollbackFor = Exception.class)
    public void deleteByOperationId(Long operationId) {
        List<Long> caseIds = caseDao.getIdsByOperationId(operationId);
        timelineService.deleteBatchByOperationId(operationId);
        for (Long id:caseIds){
            caseToolsService.deleteBatchByCaseId(id);
            caseDao.deleteById(id);
        }
    }

    /**
     * 返回简略信息
     * @param operationId Long
     * @return CaseDTO
     */
    @Override
    public List<CaseDTO> getCasesByOperationId(Long operationId) {
        List<Case> cases = caseDao.getCasesByOperationId(operationId);
        return CaseMapper.INSTANCE.casesToDTOs(cases);
    }

    /**
     * 返回Operation包含的Case的进度，成效
     *
     * @param operationId Long
     * @return Map
     */
    @Override
    public Map<String, List<Map<String, Object>>> getBarByOperationId(Long operationId) {
        List<Map<String,Object>> progress = caseDao.getProgressByOperationId(operationId);
        List<Map<String,Object>> outcome = caseDao.getOutcomeByOperationId(operationId);
        Map<String,List<Map<String,Object>>> result = new HashMap<>();
        result.put("progress",progress);
        result.put("outcome",outcome);
        return result;
    }
}
