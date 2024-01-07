package com.littlepants.attack.attackplus.utils;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2024/1/5
 */

public class MQUtils {
    public static Message rpc(RabbitTemplate rabbitTemplate, Map<String,Object> msg){
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration("10000");
        messageProperties.setCorrelationId(correlationData.getId());
        Message message = new Message(JsonUtil.toString(msg).getBytes(),messageProperties);
        return rabbitTemplate.sendAndReceive("direct","direct",message,correlationData);
    }
}
