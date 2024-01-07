package com.littlepants.attack.attackweb.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.TypeReference;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/9/22
 * @description Json类型转换工具类
 */
public class JsonUtils {
    public static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 将对象序列化为json
     * @param obj
     * @return
     */
    public static String toString(Object obj){
        if (obj == null)
            return null;
        if (obj.getClass() == String.class)
            return (String) obj;
        try {
            return mapper.writeValueAsString(obj);
        }catch (JsonProcessingException e){
            logger.error("json序列化出错:"+obj,e);
            return null;
        }
    }

    /**
     * 将Json反序列化为对象
     * @param json
     * @param tClass
     * @return
     * @param <T>
     */
    public static <T> T toBean(String json,Class<T> tClass){
        try {
            return mapper.readValue(json,tClass);
        }catch (IOException e){
            logger.error("json解析出错:"+json,e);
            return null;
        }
    }

    /**
     * 将Json数组反序列化为链表
     * @param json
     * @param eClass
     * @return
     * @param <E>
     */
    public static <E> List<E> toList(String json,Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            logger.error("json解析出错:" + json, e);
            return null;
        }
    }

    /**
     * 将Json反序列化为Map
     * @param json
     * @param kClass
     * @param vClass
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K,V>Map<K,V> toMap(String json,Class<K> kClass,Class<V> vClass){
        try {
            return mapper.readValue(json,mapper.getTypeFactory().constructMapType(Map.class,kClass,vClass));
        }catch (IOException e){
            logger.error("json解析出错"+json,e);
            return null;
        }
    }

}
