package com.littlepants.attack.attackweb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfigProperty {
    private String host;
    private String password;
    private int port;
    private int database;
    private int timeout;
}
