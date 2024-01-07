package com.littlepants.attack.attackplus.service.impl;

import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.annotation.MultiTransaction;
import com.littlepants.attack.attackplus.dao.HostDao;
import com.littlepants.attack.attackplus.entity.Host;
import com.littlepants.attack.attackplus.entity.graph.Asset;
import com.littlepants.attack.attackplus.repository.AssetRepository;
import com.littlepants.attack.attackplus.service.HostService;
import com.littlepants.attack.attackplus.utils.JsonUtil;
import com.littlepants.attack.attackplus.utils.MQUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 每个场景包含的IP 服务实现类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Service
@Slf4j
public class HostServiceImpl extends ServiceImpl<HostDao,Host> implements HostService {
    private final HostDao hostDao;
    private final RabbitTemplate directRabbitTemplate;
    private final AssetRepository assetRepository;
    public HostServiceImpl(HostDao hostDao, RabbitTemplate directRabbitTemplate, AssetRepository assetRepository){
        this.hostDao = hostDao;
        this.directRabbitTemplate = directRabbitTemplate;
        this.assetRepository = assetRepository;
    }

    /**
     * 将系统代理和IP对应，并存入数据库
     * @param topologyId Long
     */
    @Override
    @MultiTransaction
    public void matchPawAndIp(Long topologyId) throws TimeoutException {
        Map<String,Object> map = new HashMap<>();
        map.put("method","match");
        Message response = MQUtils.rpc(directRabbitTemplate,map);
        String result;
        if (response!=null){
            result = new String(response.getBody());
            log.info("请求成功，返回的结果为：{}" , result);
            Map<String,Map> res = JsonUtil.toMap(result,String.class,Map.class);
            for (String key:res.keySet()){
                Map m = res.get(key);
                Host host = new Host();
                host.setHostName(m.getOrDefault("host_name","未知").toString());
                host.setPaw(m.get("paw").toString());
                QueryWrapper<Host> queryWrapper = new QueryWrapper<>();
                //group_name 会变不应该作为唯一键
                queryWrapper.eq("group_name",key);
                hostDao.update(host,queryWrapper);
            }
            for (Host host:deployedAgent(topologyId)){
                assetRepository.updateAssetNameByIp(host.getIp(), host.getHostName(),host.getPaw());
            }
        }else {
            log.error("请求超时");
            //为了方便jmeter测试，这里抛出异常
            throw new TimeoutException("请求超时");
        }
    }

    /**
     * 根据IP获取多个HostId
     * @param ips List
     * @return List
     */
    @Override
    public List<Long> getIdsByIps(List<String> ips) {
        return hostDao.getIdsByIps(ips);
    }

    /**
     * 根据ID获取多个IP对应的caldera paw
     * @param ids List
     * @return List
     */
    @Override
    public List<String> getPawsByIds(List<Long> ids) {
        return hostDao.getPawsByIds(ids);
    }

    /**
     * 根据paw获取对应的IP
     * @param paw String
     * @return String
     */
    @Override
    public String getIpByPaw(String paw) {
        QueryWrapper<Host> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("paw",paw);
        Host host = hostDao.selectOne(queryWrapper);
        return host.getIp();
    }

    /**
     * 根据TopologyId 获取当前场景的Hosts
     * @param topologyId Long
     * @return List
     */
    @Override
    public List<Host> getHostsByTopology(Long topologyId) {
        return hostDao.getHostsByTopology(topologyId);
    }

    /**
     * 根据TopologyId 获取已部署agent的Host
     * @param topologyId Long
     * @return List
     */
    @Override
    public List<Host> deployedAgent(Long topologyId) {
        QueryWrapper<Host> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("paw","default");
        return hostDao.selectList(queryWrapper);
    }

    /**
     * 根据OperationId获取当前Operation的目标Host
     * @param operationId Long
     * @return List
     */
    @Override
    public List<Host> getHostsByOperation(Long operationId) {
        return hostDao.getHostsByOperation(operationId);
    }
}
