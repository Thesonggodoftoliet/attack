package com.littlepants.attack.attackplus.config;


import com.littlepants.attack.attackplus.AttackPlusApplication;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

/**
 * <p>
 *  单独配置事务，避免冲突
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/25
 */
@Aspect
@Slf4j
@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    // 1. 此处为了修改默认事务，必须改。加载了Neo4J依赖库之后，transactionManager变成Neo4jTransactionManager，不增加此处，启动会报错，Mysql无法使用。
    @Bean("transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    //2. Neo4j的事务管理
    @Bean("neo4jTransactionManager")
    public Neo4jTransactionManager neo4jTransactionManager(Driver driver){
        return new Neo4jTransactionManager(driver);
    }


    //3. mysql+neo4j事务管理
    @Around("@annotation(com.littlepants.attack.attackplus.annotation.MultiTransaction)")
    public Object multiTransactionManager(ProceedingJoinPoint point){
        Neo4jTransactionManager neo4jTransactionManager = AttackPlusApplication.applicationContext.getBean(Neo4jTransactionManager.class);
        DataSourceTransactionManager dataSourceTransactionManager =
                AttackPlusApplication.applicationContext.getBean(DataSourceTransactionManager.class);
        TransactionStatus neo4j = neo4jTransactionManager.getTransaction(new DefaultTransactionDefinition());
        TransactionStatus mysql = dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //先执行neo4j事务，因此mysql 事务是包含在neo4j中的，故先弹出mysql 事务
        try {
            Object object = point.proceed();
            dataSourceTransactionManager.commit(mysql);
            neo4jTransactionManager.commit(neo4j);
            log.info("事务提交成功");
            return object;
        }catch (Throwable throwable){
            dataSourceTransactionManager.rollback(mysql);
            neo4jTransactionManager.rollback(neo4j);
            log.error("事务提交失败");
            throw new RuntimeException(throwable);
        }
    }

}
