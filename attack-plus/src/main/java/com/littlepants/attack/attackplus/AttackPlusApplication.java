package com.littlepants.attack.attackplus;


import com.littlepants.attack.attackplus.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableOpenApi
public class AttackPlusApplication {
    public static ApplicationContext applicationContext;
    public static void main(String[] args) {
       applicationContext =  SpringApplication.run(AttackPlusApplication.class, args);
    }

}
