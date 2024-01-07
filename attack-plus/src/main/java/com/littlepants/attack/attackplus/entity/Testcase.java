package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/7/5
 */
@Data
@TableName("att_testcase_view")
@ApiModel(value = "Testcase", description = "测试用例")
public class Testcase implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField("testcase_name")
    private String testcaseName;

    @TableField("technique_id")
    private String techniqueId;

    @TableField("technique_name")
    private String techniqueName;

    @TableField("testcase_des")
    private String testcaseDes;

    private String tactic;

    private String platform;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
