package com.littlepants.attack.attackplus.event;

import com.littlepants.attack.attackplus.dto.OptLogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/4/11
 * @description 定义系统日志事件
 */
public class SysLogEvent extends ApplicationEvent {
    public SysLogEvent(OptLogDTO optLogDTO) {
        super(optLogDTO);
    }
}
