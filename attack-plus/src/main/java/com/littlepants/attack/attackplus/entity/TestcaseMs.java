package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * Metasploit测试用例
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Data
@TableName("att_testcase_ms")
@ApiModel(value = "TestcaseMs", description = "Metasploit测试用例")
public class TestcaseMs implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("ms 名")
    @TableField("testcase_name")
    private String testcaseName;

    @TableField("tactic")
    private String tactic;

    @TableField("technique_name")
    private String techniqueName;

    @TableField("technique_id")
    private String techniqueId;

    @TableField("platform")
    private String platform;

    @TableField("testcase_des")
    private String testcaseDes;

    @TableField("ms_id")
    private String msId;
}
