package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "campaign",autoResultMap = true)
public class Campaign {
    @TableId
    private String id;

    @JsonIgnore
    @TableField(value = "create_time")
    private Timestamp createTime;

    @JsonIgnore
    @TableField(value = "update_time")
    private Timestamp updateTime;

    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z\\d_][\\s\\u4E00-\\u9FA5A-Za-z\\d_]{1,59}$",message = "{campaign.name.validation}")
    @TableField(value = "campaign_name")
    private String campaignName;

    @TableField(value = "team_ids",typeHandler = JacksonTypeHandler.class)
    private List<String> teamIds;//此次行动包含的队伍


    @Size(max = 200,message = "{campaign.description.size}")
    @TableField(value = "campaign_description")
    private String campaignDescription;

    @TableField(value = "testcase_ids",typeHandler = JacksonTypeHandler.class)
    private List<String> testcaseIds;//此次行动包含的测试用例

    @TableField(value = "assessment_id")
    private String assessmentId;

    @TableField(exist = false)
    private int testcaseCounts;

    private double progress;

    @TableField(value = "count_of_success")
    private int countOfSuccess;

    @TableField(value = "count_of_fail")
    private int countOfFail;

    public void resetData(){
        id = UUIDGenerator.generateUUID();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        createTime = timestamp;
        updateTime = timestamp;
        campaignName = campaignName+"副本";
        teamIds = null;
        testcaseIds = null;
    }
}
