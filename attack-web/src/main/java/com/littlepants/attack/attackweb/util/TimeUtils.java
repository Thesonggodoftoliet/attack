package com.littlepants.attack.attackweb.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/9/22
 * @description 时间工具类
 */
public class TimeUtils {
    private static final SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 时间戳转换为String
     * @param timestamp
     * @return
     */
    public static String stampToString(Timestamp timestamp){
        return simpleDateFormat.format(timestamp);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getTimeString(){
        return simpleDateFormat.format(new Date());
    }
}
