package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * Atomic测试用例
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Data
@TableName("att_testcase_atomic")
@ApiModel(value = "TestcaseAtomic", description = "Atomic测试用例")
public class TestcaseAtomic implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("testcase_name")
    private String testcaseName;

    @TableField("tactic")
    private String tactic;

    @TableField("technique_name")
    private String techniqueName;

    @TableField("technique_id")
    private String techniqueId;

    @TableField("testcase_des")
    private String testcaseDes;

    @TableField("uuid")
    private String uuid;

    @TableField("supported_platforms")
    private String supportedPlatforms;

    @TableField("platform")
    private String platform;
}
