package com.littlepants.attack.attackplus.serializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.oschina.j2cache.util.Serializer;

import java.io.IOException;

/**
 * J2Cache Jackson序列化器
 *
 * @author luohq
 * @date 2023-03-15 15:48
 */
public class J2cacheJacksonSerializer implements Serializer {

    private final ObjectMapper om;

    public J2cacheJacksonSerializer() {
        this.om = new ObjectMapper();
        //设置可见性 - 全部属性、全部权限
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //设置序列化Json中包含对象类型
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        //忽略空Bean转json的错误
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //忽略未知属性，防止json字符串中存在，java对象中不存在对应属性的情况出现错误
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //注册一个时间序列化及反序列化的处理模块，用于解决jdk8中localDateTime等的序列化问题
        om.registerModule(new JavaTimeModule());

    }

    @Override
    public String name() {
        return "jackson";
    }

    @Override
    public byte[] serialize(Object obj) throws IOException {
        return om.writeValueAsBytes(obj);
    }

    @Override
    public Object deserialize(byte[] bytes) throws IOException {
        return om.readValue(bytes, Object.class);
    }
}
