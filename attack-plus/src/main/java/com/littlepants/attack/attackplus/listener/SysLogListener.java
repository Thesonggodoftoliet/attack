package com.littlepants.attack.attackplus.listener;

import com.littlepants.attack.attackplus.dto.OptLogDTO;
import com.littlepants.attack.attackplus.event.SysLogEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/4/11
 * @description 异步监听日志事件
 */
@Slf4j
@AllArgsConstructor
public class SysLogListener {

    private Consumer<OptLogDTO> consumer;
    @Async//异步处理
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        OptLogDTO sysLog = (OptLogDTO) event.getSource();
        consumer.accept(sysLog);
        //将日志信息保存到数据库...
    }
}
