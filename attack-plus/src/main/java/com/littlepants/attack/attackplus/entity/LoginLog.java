package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 登录日志表
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-04-23
 */
@Data
@TableName("att_login_log")
@ApiModel(value = "LoginLog", description = "登录日志表")
public class LoginLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("操作IP")
    @TableField("request_ip")
    private String requestIp;

    @ApiModelProperty("登陆人ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("登陆人账号")
    @TableField("user_acct")
    private String userAcct;

    @ApiModelProperty("登录操作描述")
    @TableField("log_description")
    private String logDescription;

    @ApiModelProperty("登录时间")
    @TableField("login_time")
    private LocalDateTime loginTime;

    @ApiModelProperty("浏览器请求头")
    @TableField("ua")
    private String ua;

    @ApiModelProperty("浏览器名称")
    @TableField("browser")
    private String browser;

    @ApiModelProperty("浏览器版本")
    @TableField("browser_version")
    private String browserVersion;

    @ApiModelProperty("操作系统")
    @TableField("os")
    private String os;

    @ApiModelProperty("登录地点")
    @TableField("location")
    private String location;
}
