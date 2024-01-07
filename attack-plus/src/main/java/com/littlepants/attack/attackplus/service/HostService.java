package com.littlepants.attack.attackplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.littlepants.attack.attackplus.entity.Host;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 每个场景包含的IP 服务类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
public interface HostService extends IService<Host>{
    void matchPawAndIp(Long topologyId) throws TimeoutException;
    List<Long> getIdsByIps(List<String> ips);
    List<String> getPawsByIds(List<Long> ids);
    String getIpByPaw(String paw);
    List<Host> getHostsByTopology(Long topologyId);
    List<Host> deployedAgent(Long topologyId);
    List<Host> getHostsByOperation(Long operationId);
}
