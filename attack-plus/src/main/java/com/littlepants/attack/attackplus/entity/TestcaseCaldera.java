package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * Caldera能力

 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Data
@TableName("att_testcase_caldera")
@ApiModel(value = "TestcaseCaldera", description = "Caldera能力")
public class TestcaseCaldera implements Serializable {
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

    @TableField("ability_id")
    private String abilityId;

    @TableField("supported_platforms")
    private String supportedPlatforms;

    @TableField("platform")
    private String platform;
}
