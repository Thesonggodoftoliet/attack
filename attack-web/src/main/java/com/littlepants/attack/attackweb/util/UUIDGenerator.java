package com.littlepants.attack.attackweb.util;

import java.util.UUID;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/9/22
 * @description UUID生成类
 */
public class UUIDGenerator {
    /**
     * 生成UUID的String
     * @return
     */
    public static String get(){
        return UUID.randomUUID().toString();
    }

    /**
     * 产生UUID
     * @return
     */
    public static String generateUUID(){
        String uuid = UUIDGenerator.get();
        return uuid.replace("-","");
    }
}
