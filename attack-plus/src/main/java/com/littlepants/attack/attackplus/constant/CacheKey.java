package com.littlepants.attack.attackplus.constant;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 用于同一管理和生成缓存的region和key， 避免多个项目使用的key重复
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/23
 */

public interface CacheKey {
    /**
     * token 前缀
     * 完整key: user:{key}->str
     */
    String USER = "user";
    /**
     * 验证码 前缀
     * 完整key: captcha:{key} -> str
     */
    String CAPTCHA = "captcha";
    /**
     * 资源 前缀
     * 完整key: resource:{resourceId} -> obj
     */
    String RESOURCE = "resource";

    /**
     * 用户拥有的资源 前缀
     * 完整key: user_resource:{userId} -> [RESOURCE_ID, ...]
     */
    String USER_RESOURCE = "user_resource";
    /**
     * 所有需要鉴权的资源
     */
    String RESOURCE_NEED_TO_CHECK = "resource_need_to_check";
    /**
     * 构建key
     */
    static String buildKey(Object... args) {
        if (args.length == 1) {
            return String.valueOf(args[0]);
        } else if (args.length > 0) {
            return StrUtil.join(StrPool.COLON, args);
        } else {
            return "";
        }
    }
}
