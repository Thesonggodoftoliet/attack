package com.littlepants.attack.attackplus.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *  登录参数
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/24
 */
@Data
public class LoginParamDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 验证码 key
     */
    private String key;
    /**
     * 验证码
     */
    private String code;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
}
