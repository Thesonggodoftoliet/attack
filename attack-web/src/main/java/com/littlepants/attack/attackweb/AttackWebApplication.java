package com.littlepants.attack.attackweb;

import com.littlepants.attack.attackweb.config.RsaKeyProperties;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@MapperScan("com.littlepants.attack.attackweb.mapper")
@EnableCaching
@EnableTransactionManagement
@EnableDubbo
public class AttackWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(AttackWebApplication.class, args);
    }

}
