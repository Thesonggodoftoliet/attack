package com.littlepants.attack.attackplus.annotation;

import java.lang.annotation.*;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/4/12
 * @description 操作日志注解
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 描述
     *
     * @return {String}
     */
    String value();

    /**
     * 记录执行参数
     *
     * @return
     */
    boolean recordRequestParam() default true;

    /**
     * 记录返回参数
     *
     * @return
     */
    boolean recordResponseParam() default true;
}
