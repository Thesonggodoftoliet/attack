package com.littlepants.attack.attackplus.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *  UserDTO
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/24
 */

@Data
public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String phone;
    private String userAccount;
    private String nickName;
    private String email;
    private Boolean status;
    private String lastLoginTime;
}
