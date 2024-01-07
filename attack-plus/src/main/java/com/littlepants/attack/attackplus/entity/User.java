package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-04-21
 */
@Data
@TableName("att_user")
@ApiModel(value = "User", description = "用户表")
public class User implements Serializable, UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID，后端雪花算法生成")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("用户手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("用户账号，用于登录")
    @TableField("user_acct")
    private String userAcct;

    @ApiModelProperty("用户昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty("用户邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("用户密码")
    @TableField("user_pwd")
    private String userPwd;

    @ApiModelProperty("用户的封禁状态 1为启用 0为封禁")
    @TableField("enable_state")
    private Boolean enableState;

    @ApiModelProperty("用户创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("用户归属哪个团队ID")
    @TableField("team_id")
    private Long teamId;

    @ApiModelProperty("用户最后登录时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<Role> roles;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return userPwd;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return userAcct;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return enableState;
    }
}
