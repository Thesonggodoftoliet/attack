package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.littlepants.attack.attackweb.validation.EditUserInfoValidation;
import com.littlepants.attack.attackweb.validation.PasswordChangeValidation;
import com.littlepants.attack.attackweb.validation.RegisterValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Getter
@Setter
@ApiModel
@TableName(value = "sys_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {
    @TableId
    @ApiModelProperty(value = "用户ID",example = "c496d0a637694e669e3352b84380a9cd")
    private String id;

    @TableField(value = "username")
    @ApiModelProperty(value = "用户名",example = "attack_web")
    @Pattern(regexp = "^[a-zA-Z]\\w{4,15}$",message = "{user.username.validation}",
            groups = RegisterValidation.class)
    private String username;

    @TableField(value = "nickname")
    @ApiModelProperty(value = "用户昵称",example = "管理员")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z\\d_]{2,10}$",message = "{user.nickname.validation}",
            groups = {EditUserInfoValidation.class,RegisterValidation.class})
    private String nickName;

    @ApiModelProperty(value = "用户手机号",example = "\"19911112222\"")
    @Pattern(regexp = "^(13\\d|14[5|7]|15[0|1|2|3|5|6|7|8|9]|17[7|3|6|8]|18[0|1|2|3|5|6|7|8|9]|19[8|9])\\d{8}$",
            message = "{user.phone.validation}", groups = {EditUserInfoValidation.class,RegisterValidation.class})
    private String phone;

    @ApiModelProperty(value = "用户邮箱",example = "a123456789@gmail.com")
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",message = "{user.email.validation}",
            groups ={EditUserInfoValidation.class,RegisterValidation.class} )
    private String email;

    @TableField(value = "userpwd")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,26}$",message = "{user.password.validation}",
            groups ={RegisterValidation.class} )
    @ApiModelProperty(value = "用户密码",example = "AttackWeb123")
    private String password;

    @TableField(value = "user_status")
    @ApiModelProperty(value = "用户状态",example = "active")
    private String userStatus;

    @TableField(value = "last_password_change")
    @ApiModelProperty(hidden = true)
    private Timestamp lastPasswordChange;

    @DecimalMax(value = "3",message = "{user.group.validation}",
            groups = {EditUserInfoValidation.class})
    @DecimalMin(value = "1",message = "{user.group.validation}",
            groups = {EditUserInfoValidation.class})
    @TableField(value = "group_id")
    @ApiModelProperty(value = "分组编号",allowableValues = "1,2,3",example = "1")
    private Integer groupId;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String group;

    @TableField(exist = false)
    @ApiModelProperty(value = "策略组",hidden = true)
    private List<PolicyGroup> group_id = new ArrayList<>();

    @TableField(exist = false)
    @ApiModelProperty(value = "新密码",example = "AttackWeb.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,26}$",message = "{user.password.validation}",
            groups ={PasswordChangeValidation.class} )
    private String resetPassword;

    @TableField(value = "last_login")
    @ApiModelProperty(value = "上次登录时间",example = "测试需删除参数'lastPasswordChange'",hidden = true)
    private String lastLogin;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return group_id;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password){
        this.password = password;
    }

    public String userPassword(){
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }


}
