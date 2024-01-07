package com.littlepants.attack.attackplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.littlepants.attack.attackplus.dto.CaseCalderaDTO;
import com.littlepants.attack.attackplus.dto.CaseDTO;
import com.littlepants.attack.attackplus.entity.Case;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
public interface CaseService extends IService<Case>{
    void assignTools(Long caseId, List<Long> tools);
    void deleteById(Long caseId);
    List<Long> getIdsByOperationAndAbility(Long operationId,String abilityId);
    void updateFromCaldera(List<CaseCalderaDTO> cases);
    void deleteByOperationId(Long operationId);
    List<CaseDTO> getCasesByOperationId(Long operationId);
}
