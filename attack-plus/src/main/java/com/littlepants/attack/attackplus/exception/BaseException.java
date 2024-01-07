package com.littlepants.attack.attackplus.exception;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/4/12
 * @description
 */
public interface BaseException{
    /**
     * 统一参数验证异常码
     */
    int BASE_VALID_PARAM = -9;

    /**
     *  返回异常信息
     * @return
     */
    String getMessage();

    /**
     * 返回异常编码
     * @return
     */
    int getCode();
}
