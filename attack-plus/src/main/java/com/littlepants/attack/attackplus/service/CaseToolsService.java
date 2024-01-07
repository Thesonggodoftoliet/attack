package com.littlepants.attack.attackplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.littlepants.attack.attackplus.entity.CaseTools;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-07
 */
public interface CaseToolsService extends IService<CaseTools>{
    void deleteBatchByCaseId(Long caseId);
    void deleteBatchByToolId(Long toolId);
}
