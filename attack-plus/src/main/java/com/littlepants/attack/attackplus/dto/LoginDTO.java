package com.littlepants.attack.attackplus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  登录信息
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/24
 */

@Data
@AllArgsConstructor
public class LoginDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UserDTO userInfo;
    private String token;
    private List<String> roles;
}
