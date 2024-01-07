package com.littlepants.attack.attackplus.service.impl;

import cn.hutool.core.date.StopWatch;
import cn.hutool.http.HttpRequest;
import com.littlepants.attack.attackplus.entity.*;
import com.littlepants.attack.attackplus.externalModel.caldera.Adversary;
import com.littlepants.attack.attackplus.externalModel.caldera.Planner;
import com.littlepants.attack.attackplus.externalModel.caldera.Source;
import com.littlepants.attack.attackplus.mapper.TestcaseMapper;
import com.littlepants.attack.attackplus.service.CampaignService;
import com.littlepants.attack.attackplus.service.TestcaseCalderaService;
import com.littlepants.attack.attackplus.service.TestcaseStrategy;
import com.littlepants.attack.attackplus.utils.JsonUtil;
import com.littlepants.attack.attackplus.utils.MQUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * Caldera能力策略
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Slf4j
@Service("caldera")
public class TestcaseCalderaStrategy implements TestcaseStrategy<TestcaseCaldera> {
    private final TestcaseCalderaService calderaService;
    private final CampaignService campaignService;
    private final RabbitTemplate directRabbitTemplate;

    public TestcaseCalderaStrategy(TestcaseCalderaService calderaService, CampaignService campaignService, RabbitTemplate directRabbitTemplate) {
        this.calderaService = calderaService;
        this.campaignService = campaignService;
        this.directRabbitTemplate = directRabbitTemplate;
    }

    @Override
    public String getStrategy() {
        return "CalderaStrategy";
    }

    @Override
    public List<TestcaseCaldera> getTestcasesByIds(List<Long> ids) {
        return calderaService.listByIds(ids);
    }

    @Override
    public List<Testcase> toTestcases(List<TestcaseCaldera> specificTestcase) {
        List<Testcase> testcaseList = new ArrayList<>();
        for (TestcaseCaldera caldera:specificTestcase){
            Testcase testcase = TestcaseMapper.INSTANCE.calderaToTestcase(caldera);
            testcase.setPlatform("caldera");
            testcaseList.add(testcase);
        }
        return testcaseList;
    }

    @Override
    public List<Case> toCases(List<TestcaseCaldera> specificTestcases, Long operationId) {
        List<Case> testcaseList = new ArrayList<>();
        for (TestcaseCaldera caldera:specificTestcases){
            testcaseList.add(new Case(TestcaseMapper.INSTANCE.calderaToTestcase(caldera),operationId));
        }
        return testcaseList;
    }

    @Override
    public List<Testcase> getAllTestcases() {
        List<TestcaseCaldera> testcaseCalderas = calderaService.list();
        return toTestcases(testcaseCalderas);
    }

    @Override
    public List<Testcase> getTestcasesBy(Map<String, Object> params) {
        List<TestcaseCaldera> testcaseCalderas = calderaService.listByMap(params);
        return toTestcases(testcaseCalderas);
    }

    @Override
    public void execute(Operation operation) throws TimeoutException {
        Campaign campaign = campaignService.getById(operation.getCampaignId());
        Map<String,String> data = new HashMap<>();
        data.put("operationName",operation.getOperationName());
        data.put("adversaryId", campaign.getAdversaryId());
        data.put("operationId", String.valueOf(operation.getId()));
        Map<String,Object> msg = new HashMap<>();
        msg.put("data",data);
        msg.put("method","executeOperation");
        log.info("远程调用的方法为：{},数据为：{}","executeOperation",data);
        Message response = MQUtils.rpc(directRabbitTemplate,msg);
        String result;
        if (response != null) {
            result = new String(response.getBody());
            log.info("请求成功，返回的结果为：{}" , result);
        }else {
            log.error("请求超时");
            //为了方便jmeter测试，这里抛出异常
            throw new TimeoutException("请求超时");
        }

        if (result.equals("fail"))
            throw new RuntimeException("Operation执行出错");
    }


}
