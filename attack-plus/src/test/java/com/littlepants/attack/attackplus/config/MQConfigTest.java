package com.littlepants.attack.attackplus.config;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@SpringBootTest
@Slf4j
class MQConfigTest {
    @Autowired
    private RabbitTemplate directRabbitTemplate;

    @Test
    public void testRPC() throws TimeoutException {
        log.info("请求报文：{}" , "NIHAO");
        //请求结果
        String result = null;
        //设置消息唯一id
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        //直接发送message对象
        MessageProperties messageProperties = new MessageProperties();
        //过期时间10秒,也是为了减少消息挤压的可能
        messageProperties.setExpiration("10000");
        messageProperties.setCorrelationId(correlationId.getId());
        Message message = new Message("NIHAO".getBytes(), messageProperties);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("direct模式下rpc请求耗时");
        Message response = directRabbitTemplate.sendAndReceive("direct", "direct", message, correlationId);
        stopWatch.stop();
        log.info(stopWatch.getLastTaskName()+"：" + stopWatch.getTotalTimeMillis());

        if (response != null) {
            result = new String(response.getBody());
            log.info("请求成功，返回的结果为：{}" , result);
        }else {
            log.error("请求超时");
            //为了方便jmeter测试，这里抛出异常
            throw new TimeoutException("请求超时");
        }
    }

    @Test
    public void testRPC1() throws TimeoutException {
        log.info("请求报文：{}" , "NIHAO");
        //请求结果
        String result = null;
        //设置消息唯一id
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        //直接发送message对象
        MessageProperties messageProperties = new MessageProperties();
        //过期时间10秒,也是为了减少消息挤压的可能
        messageProperties.setExpiration("10000");
        messageProperties.setCorrelationId(correlationId.getId());
        Message message = new Message("NIHAO".getBytes(), messageProperties);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("direct模式下rpc请求耗时");
        Message response = directRabbitTemplate.sendAndReceive("direct", "direct", message, correlationId);
        stopWatch.stop();
        log.info(stopWatch.getLastTaskName()+"：" + stopWatch.getTotalTimeMillis());

        if (response != null) {
            result = new String(response.getBody());
            log.info("请求成功，返回的结果为：{}" , result);
        }else {
            log.error("请求超时");
            //为了方便jmeter测试，这里抛出异常
            throw new TimeoutException("请求超时");
        }
    }

}