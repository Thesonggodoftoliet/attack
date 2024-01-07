package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-04-23
 */
@Data
@TableName("att_opt_log")
@ApiModel(value = "OptLog", description = "操作日志表")
public class OptLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("操作IP")
    @TableField("request_ip")
    private String requestIp;

    @ApiModelProperty("日志类型 OPT 操作类型  EX 异常类型")
    @TableField("log_type")
    private String logType;

    @ApiModelProperty("操作人ID")
    @TableField("user_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty("操作人账号")
    @TableField("user_acct")
    private String userAcct;

    @ApiModelProperty("操作描述")
    @TableField("log_description")
    private String logDescription;

    @ApiModelProperty("类路径")
    @TableField("class_path")
    private String classPath;

    @ApiModelProperty("请求方法")
    @TableField("action_method")
    private String actionMethod;

    @ApiModelProperty("请求地址")
    @TableField("request_uri")
    private String requestUri;

    @ApiModelProperty("请求类型")
    @TableField("http_method")
    private String httpMethod;

    @ApiModelProperty("请求参数")
    @TableField("params")
    private String params;

    @ApiModelProperty("返回值")
    @TableField("result")
    private String result;

    @ApiModelProperty("异常详情信息")
    @TableField("ex_desc")
    private String exDesc;

    @ApiModelProperty("异常描述")
    @TableField("ex_detail")
    private String exDetail;

    @ApiModelProperty("开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    @TableField("finish_time")
    private LocalDateTime finishTime;

    @ApiModelProperty("消耗时间")
    @TableField("consuming_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long consumingTime;

    @ApiModelProperty("浏览器请求头")
    @TableField("ua")
    private String ua;
}
