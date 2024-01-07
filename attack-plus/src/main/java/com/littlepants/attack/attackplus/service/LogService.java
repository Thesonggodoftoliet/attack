package com.littlepants.attack.attackplus.service;

import com.littlepants.attack.attackplus.dto.OptLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/12
 * @description
 */
@Service
@Slf4j
public class LogService {
    //将日志信息保存到数据库
    public void saveLog(OptLogDTO optLogDTO){
        //此处只是将日志信息进行输出，实际项目中可以将日志信息保存到数据库
        log.debug("保存日志信息：" + optLogDTO);
    }
}
