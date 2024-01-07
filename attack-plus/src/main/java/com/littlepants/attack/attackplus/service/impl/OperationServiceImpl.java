package com.littlepants.attack.attackplus.service.impl;

import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.dao.OperationDao;
import com.littlepants.attack.attackplus.entity.Case;
import com.littlepants.attack.attackplus.entity.Host;
import com.littlepants.attack.attackplus.entity.Operation;
import com.littlepants.attack.attackplus.entity.OperationIp;
import com.littlepants.attack.attackplus.service.*;
import com.littlepants.attack.attackplus.utils.JsonUtil;
import com.littlepants.attack.attackplus.service.OperationService;
import com.littlepants.attack.attackplus.service.TestcaseCampaignService;
import com.littlepants.attack.attackplus.utils.MQUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 对应Caldera的 operations (campaign)服务实现类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Service
@Slf4j
public class OperationServiceImpl extends ServiceImpl<OperationDao, Operation> implements OperationService {
    private final RabbitTemplate directRabbitTemplate;
    private final ApplicationContext applicationContext;
    private final CampaignService campaignService;
    private final OperationDao operationDao;
    private final CaseService caseService;
    private final OperationIpService operationIpService;
    private final HostService hostService;
    private final TestcaseCampaignService testcaseCampaignService;

    public OperationServiceImpl(RabbitTemplate directRabbitTemplate, ApplicationContext applicationContext,
                                CampaignService campaignService, OperationDao operationDao, CaseService caseService,
                                OperationIpService operationIpService, HostService hostService,
                                TestcaseCampaignService testcaseCampaignService) {
        this.directRabbitTemplate = directRabbitTemplate;
        this.applicationContext = applicationContext;
        this.campaignService = campaignService;
        this.operationDao = operationDao;
        this.caseService = caseService;
        this.operationIpService = operationIpService;
        this.hostService = hostService;
        this.testcaseCampaignService = testcaseCampaignService;
    }

    /**
     * 从测试方案模板生成测试方案
     * @param operation Operation
     */
    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void addOperationFromCampaign(Operation operation) {
        operation.setState(0);
        operationDao.insert(operation);
    }

    /**
     * 分配IP给该执行方案
     * @param operation Operation
     * @param ips List<Long>
     */
    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void assignIps(Operation operation, List<Long> ips) throws TimeoutException {
        List<OperationIp> hosts = new ArrayList<>();
        for (Long id:ips)
            hosts.add(new OperationIp(id,operation.getId()));
        operationIpService.saveBatch(hosts);
        List<String> paws = hostService.getPawsByIds(ips);
        Map<String,Object> params = new HashMap<>();
        params.put("group",String.valueOf(operation.getId()));
        params.put("paws",paws);
        Map<String,Object> msg = new HashMap<>();
        msg.put("data",params);
        msg.put("method","assign");
        Message response = MQUtils.rpc(directRabbitTemplate,msg);
        String result;
        if (response!=null){
            result = new String(response.getBody());
            log.info("请求成功，返回的结果为:{}",result);
        }else {
            log.error("请求超时");
            throw new TimeoutException("请求超时");
        }
        if (result.equals("Fail"))
            throw new RuntimeException("分配IP时出现错误");
    }

    @Override
    @Transactional(transactionManager = "transactionManager",rollbackFor = Exception.class)
    public void deleteOperation(Long id) {
        Map<String,Object> params = new HashMap<>();
        params.put("operation_id",id);
        operationIpService.removeByMap(params);//删除IP分配
        caseService.deleteByOperationId(id);
    }

    /**
     * 执行本方案
     * @param id Long
     */
    @Transactional(transactionManager = "transactionManager",rollbackFor = Exception.class)
    @Override
    public void execute(Long id) throws TimeoutException, CloneNotSupportedException {
        Operation operation = operationDao.selectById(id);
        List<String> platforms = testcaseCampaignService.getPlatformsByCampaignId(operation.getCampaignId());
        for (String platform:platforms){
            TestcaseStrategy strategy = applicationContext.getBean(platform,TestcaseStrategy.class);
            strategy.execute(operation);
        }
        Operation newOperation = new Operation();
        newOperation.setStartTime(LocalDateTime.now());
        newOperation.setState(1);
        newOperation.setId(operation.getId());
        newOperation.setOperationId(operation.getId().toString());
        operationDao.updateById(newOperation);

        // 插入执行用例
        Map<String, List<Long>> testcaseIds = campaignService.getTestcaseIdsGroupByPlatform(operation.getCampaignId());
        TestcaseStrategy testcaseStrategy;
        List<Case> caseList = new ArrayList<>();
        for (String platform:testcaseIds.keySet()){
            // 获取执行用例
            testcaseStrategy = applicationContext.getBean(platform,TestcaseStrategy.class);
            List<Case> tempList = testcaseStrategy.toCases(testcaseStrategy.getTestcasesByIds(testcaseIds.get(platform)),
                    operation.getId());
            caseList.addAll(tempList);
        }
        List<Host> hosts = hostService.getHostsByOperation(operation.getId());
        List<Case> cases = new ArrayList<>();
        for (Case temp:caseList){
            for (Host host:hosts){
                Case save = temp.clone();
                save.setTargetIp(host.getIp());
                save.setTargetHost(host.getHostName());
                cases.add(save);
            }
        }
        caseService.saveBatch(cases);
    }
}
