package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Data
@TableName("att_case")
@ApiModel(value = "Case", description = "")
public class Case implements Serializable,Cloneable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("testcase_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long testcaseId;

    @TableField("testcase_name")
    private String testcaseName;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField("technique_name")
    private String techniqueName;

    @TableField("command")
    private String command;

    @TableField("technique_id")
    private String techniqueId;

    @TableField("case_des")
    private String caseDes;

    @TableField("status")
    private Integer status;

    @TableField("tactic")
    private String tactic;

    @TableField("outcome")
    private String outcome;

    @TableField("outcome_note")
    private String outcomeNote;

    @TableField("detection_time")
    private LocalDateTime detectionTime;

    @TableField("alert_severity")
    private String alertSeverity;

    @TableField("alert_triggered")
    private String alertTriggered;

    @TableField("activity_logged")
    private String activityLogged;

    @TableField("detections")
    private String detections;

    @TableField("defends")
    private String defends;

    @TableField("operation_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long operationId;

    @TableField("source_ip")
    private String sourceIp;

    @TableField("source_host")
    private String sourceHost;

    @TableField("target_ip")
    private String targetIp;

    @TableField("target_host")
    private String targetHost;

    @TableField("case_id")
    private String caseId;

    public Case(Testcase testcase,Long operationId){
        this.testcaseId = testcase.getId();
        this.testcaseName = testcase.getTestcaseName();
        this.techniqueId = testcase.getTechniqueId();
        this.techniqueName = testcase.getTechniqueName();
        this.caseDes = testcase.getTestcaseDes();
        this.tactic = testcase.getTactic();
        this.operationId = operationId;
        this.createTime = testcase.getCreateTime();
        this.updateTime = testcase.getUpdateTime();
    }

    public Case(){

    }

    @Override
    public Case clone() throws CloneNotSupportedException {
        return (Case) super.clone();
    }
}
