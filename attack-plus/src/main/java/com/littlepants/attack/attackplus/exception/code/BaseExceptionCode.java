package com.littlepants.attack.attackplus.exception.code;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/4/12
 * @description
 */
public interface BaseExceptionCode {
    /**
     * 异常编码
     * @return
     */
    int getCode();

    /**
     * 异常消息
     * @return
     */
    String getMsg();
}
