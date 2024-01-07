package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Data
@TableName(value = "campaign_template",autoResultMap = true)
public class CampaignTemplate {
    @TableId
    private String id;

    @JsonIgnore
    @TableField(value = "create_time")
    private Timestamp createTime;

    @JsonIgnore
    @TableField(value = "update_time")
    private Timestamp updateTime;

    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z\\d_][\\s\\u4E00-\\u9FA5A-Za-z\\d_]{1,59}$",message = "{template.name.validation}")
    @TableField(value = "template_name")
    private String templateName;

    @TableField(value = "team_ids",typeHandler = JacksonTypeHandler.class)
    private List<String> teamIds;//此模板包含的队伍

    @Size(max = 200,message = "{template.description.size}")
    @TableField(value = "template_description")
    private String templateDescription;

    @TableField(value = "testcase_ids",typeHandler = JacksonTypeHandler.class)
    private List<String> testcaseIds;//此模板包含的测试用例

    @TableField(exist = false)
    private int testcaseCounts;
}
