package com.littlepants.attack.attackplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.littlepants.attack.attackplus.entity.Operation;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 对应Caldera的 operations (campaign)
 服务类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
public interface OperationService extends IService<Operation>{
    void addOperationFromCampaign(Operation operation) throws CloneNotSupportedException;
    void assignIps(Operation operation, List<Long> ips) throws TimeoutException;
    void deleteOperation(Long id);
    void execute(Long id) throws TimeoutException, CloneNotSupportedException;
}
