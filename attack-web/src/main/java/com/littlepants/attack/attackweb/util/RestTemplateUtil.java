package com.littlepants.attack.attackweb.util;

import com.littlepants.attack.attackweb.converter.CalderaMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/9/22
 * @description 用于接收Http请求的返回
 */
public class RestTemplateUtil {
    /**
     * 获取RestTemplate的实例，支持text/plain
     * @return
     */
    public static RestTemplate getInstance(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new CalderaMessageConverter());
        return restTemplate;
    }
}
