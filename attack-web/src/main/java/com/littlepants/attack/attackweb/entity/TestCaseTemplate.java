package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.littlepants.attack.attackweb.validation.TestCaseTempValidation;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;

@Data
@TableName(value = "testcase_template",autoResultMap = true)
public class TestCaseTemplate {
    @TableId
    private String id;

    @JsonIgnore
    @TableField(value = "create_time")
    private Timestamp createTime;

    @JsonIgnore
    @TableField(value = "update_time")
    private Timestamp updateTime;

    @TableField(value = "template_id")
    private String templateId;

    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z\\d_][\\s\\u4E00-\\u9FA5A-Za-z\\d_]{1,99}$",message = "{template.name.validation}",
            groups = {TestCaseTempValidation.class})
    @TableField(value = "template_name")
    private String templateName;

    @TableField(value = "redteam",typeHandler = JacksonTypeHandler.class)
    private @Valid RedTeam redTeam;

    @TableField(value = "blueteam",typeHandler = JacksonTypeHandler.class)
    private @Valid BlueTeam blueTeam;

    @TableField(value = "team_ids",typeHandler = JacksonTypeHandler.class)
    private List<String> teamIds;

    @TableField(value = "evidence_files",typeHandler = JacksonTypeHandler.class)
    private List<String> evidenceFiles;

}
