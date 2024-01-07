package com.littlepants.attack.attackweb.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/14
 * @descriptio Caldera的消息转换类
 */
public class CalderaMessageConverter extends MappingJackson2HttpMessageConverter {
    public CalderaMessageConverter(){
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.TEXT_HTML);
        setSupportedMediaTypes(mediaTypes);
    }
}
