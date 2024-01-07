package com.littlepants.attack.attackweb.config;

public final class ConstantKit {
    public static final Integer DEL_FLAG_TRUE=1;
    public static final Integer DEL_FLAG_FALSE=0;
    /**
     * redis存储token设置的过期时间
     * 单位：秒(1h)
     */
    public static final Long TOKEN_EXPIRE_TIME= (long) (60 * 60);

    /**
     * 设置可以重置token过期时间的时间界限
     * 单位：毫秒(30min)
     */
    public static final Long TOKEN_RESET_TIME= (long) (1000 * 30 * 60);
}
