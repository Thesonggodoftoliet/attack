package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-07
 */
@Data
@TableName("att_testcase_campaign")
@ApiModel(value = "TestcaseCampaign", description = "")
public class TestcaseCampaign implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField("campaign_id")
    private Long campaignId;

    @TableField("testcase_id")
    private Long testcaseId;

    @TableField("platform")
    private String platform;
    private Integer priority;

    public TestcaseCampaign(Long campaignId, Long testcaseId, String platform,Integer priority) {
        this.campaignId = campaignId;
        this.testcaseId = testcaseId;
        this.platform = platform;
        this.priority = priority;
    }
    public TestcaseCampaign(){

    }
}
