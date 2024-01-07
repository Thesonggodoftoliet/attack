package com.littlepants.attack.attackweb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

@Configuration
public class JedisConfig extends CachingConfigurerSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisConfig.class);

    @Resource
    private RedisConfigProperty redisConfigProperty;

    @Bean(name = "jedisPoolConfig")
    @ConfigurationProperties(prefix = "spring.redis.pool-config")
    public JedisPoolConfig getRedisConfig(){
        return new JedisPoolConfig();
    }

    @Bean(name = "jedisPool")
    public JedisPool jedisPool(@Qualifier(value = "jedisPoolConfig") final JedisPoolConfig jedisPoolConfig){
        LOGGER.info("Jedis Pool build start");
        String host = redisConfigProperty.getHost();
        int timeout = redisConfigProperty.getTimeout();
        String password = redisConfigProperty.getPassword();
        int database = redisConfigProperty.getDatabase();
        int port = redisConfigProperty.getPort();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,host,port,timeout,password,database);
        LOGGER.info("Jedis Pool build success host={},port={}",host,port);
        return jedisPool;
    }
}
