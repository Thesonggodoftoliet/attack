package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "activitytimeline")
public class TimeLine {
    @TableId
    private String id;

    @TableField(value = "create_time")
    private Timestamp createTime;

    @TableField(value = "campaign_id")
    private String campaignId;

    @TableField(value = "testcase_id")
    private String testcaseId;

    @TableField(value = "testcase_name")
    private String testcaseName;

    private String description;
}
