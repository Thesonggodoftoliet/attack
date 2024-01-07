package com.littlepants.attack.attackplus.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Binding;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/12/15
 */

@Configuration
@Slf4j
public class DirectReplyConfig {
    /**
     * 注意bean的名称是由方法名决定的，所以不能重复
     * @return
     */
    @Bean
    public Queue directRequest() {
        return new Queue("direct.request", true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct");
    }

    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directRequest()).to(directExchange()).with("direct");
    }


    /**
     * 当进行多个主题队列消费时，最好对每个单独定义RabbitTemplate，以便将各自的参数分别控制
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate directRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());

        //这一步非常关键
        template.setUseTemporaryReplyQueues(false);
        template.setReplyAddress("amq.rabbitmq.reply-to");
        // template.expectedQueueNames();
        template.setUserCorrelationId(true);

        //设置请求超时时间为10s
        template.setReplyTimeout(10000);
        return template;
    }
}
