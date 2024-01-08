package com.littlepants.attack.attackplus.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * MQ队列配置
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/6/5
 */

@Configuration
public class MQConfig {
    @Bean
    public DirectExchange nessusExchange() {
        return new DirectExchange("nessus");
    }

    @Bean
    public DirectExchange calderaExchange() {
        return new DirectExchange("caldera");
    }

    @Bean
    public DirectExchange rpcExchange(){
        return new DirectExchange("rpc");
    }

    @Bean
    public Queue nessusQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue calderaQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue rpcQueue() {
        return new Queue("rpc.request");
    }

    @Bean
    public Binding bindingNessus(DirectExchange nessusExchange, Queue nessusQueue) {
        return BindingBuilder.bind(nessusQueue)
                .to(nessusExchange)
                .with("graph");
    }

    @Bean
    public Binding bindingCaldera(DirectExchange calderaExchange, Queue calderaQueue) {
        return BindingBuilder.bind(calderaQueue)
                .to(calderaExchange)
                .with("timeline");
    }

    @Bean
    public Binding bindingRpc(DirectExchange rpcExchange,Queue rpcQueue){
        return BindingBuilder.bind(rpcQueue).to(rpcExchange).with("rpc");
    }


}
