package com.littlepants.attack.attackplus.controller;

import com.littlepants.attack.attackplus.annotation.SysLog;
import com.littlepants.attack.attackplus.dto.OptLogDTO;
import com.littlepants.attack.attackplus.event.SysLogEvent;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/4/11
 * @description
 */
@RestController
@RequestMapping("/user")
public class PersonController {

    @Setter
    private ApplicationContext applicationContext;

    @GetMapping("/getUser")
    public String getUser(){
        OptLogDTO logInfo = new OptLogDTO();
        logInfo.setRequestIp("127.0.0.1");
        logInfo.setUserName("admin");
        logInfo.setType("OPT");
        logInfo.setDescription("查询用户信息");

        ApplicationEvent event = new SysLogEvent(logInfo);

        applicationContext.publishEvent(event);

        return "OK";
    }

    @SysLog("分页查询用户")
    @GetMapping("/page/{pageNum}/{pageSize}")
    public OptLogDTO findByPage(@PathVariable Integer pageNum,@PathVariable Integer pageSize){
        return new OptLogDTO();
    }

    @GetMapping("/get")
    public String get(String text){
        return "处理之后的文本内容"+text;
    }
}
