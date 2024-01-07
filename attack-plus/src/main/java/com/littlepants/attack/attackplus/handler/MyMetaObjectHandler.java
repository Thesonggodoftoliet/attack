package com.littlepants.attack.attackplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 * MyBatisPlus 自动填充类实现
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/21
 */

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ...");
        LocalDateTime localDateTime = LocalDateTime.now();
        this.strictInsertFill(metaObject,"createTime", LocalDateTime.class,localDateTime);
        this.strictInsertFill(metaObject,"updateTime", LocalDateTime.class,localDateTime);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ...");
        // MetaObjectHandler提供的默认方法的策略均为:如果属性有值则不覆盖,如果填充值为null则不填充
        // 因此改为 null 就进行填充
        metaObject.setValue("updateTime",null);
        this.strictInsertFill(metaObject,"updateTime", LocalDateTime.class,LocalDateTime.now());
    }
}
