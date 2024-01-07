package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import com.littlepants.attack.attackweb.validation.EditTeamInfoValidation;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "testcase",autoResultMap = true)
public class TestCase {
    @NotNull(groups = {EditTeamInfoValidation.class}, message = "{testCase.id.validation}")
    @TableId
    private String id;

    @TableField(value = "create_time")
    @JsonIgnore
    private Timestamp createTime;

    @TableField(value = "update_time")
    @JsonIgnore
    private Timestamp updateTime;

    @TableField(value = "template_id")
    private String templateId;//使用的模板

    @TableField(value = "testcase_name")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z\\d_][\\s\\u4E00-\\u9FA5A-Za-z\\d_]{1,119}$",message = "{testCase.name.validation}")
    private String testcaseName;

    @TableField(value = "redteam",typeHandler = JacksonTypeHandler.class)
    private @Valid RedTeam redTeam;//红队配置


    @TableField(value = "blueteam",typeHandler = JacksonTypeHandler.class)
    private  @Valid BlueTeam blueTeam;//蓝队配置

    @TableField(value = "team_ids",typeHandler = JacksonTypeHandler.class)
    private List<String> teamIds;

    @TableField(value = "evidence_files",typeHandler = JacksonTypeHandler.class)
    private List<String> evidenceFiles;

    @TableField(value = "campaign_id")
    private String campaignId;

    public void resetData(){
        id = UUIDGenerator.generateUUID();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        createTime = timestamp;
        updateTime = timestamp;
        testcaseName = testcaseName+"副本";
        redTeam.setStartTime(null);
        redTeam.setEndTime(null);
        redTeam.setStatus(null);
        redTeam.setTargetAssets(null);
        redTeam.setSourceIPs(null);
        blueTeam.setOutcome(null);
        blueTeam.setOutcomeNote(null);
        blueTeam.setDetectionTime(null);
        blueTeam.setAlertSeverity(null);
        blueTeam.setAlertTriggered(null);
        blueTeam.setActivityLogged(null);
        teamIds = null;
        evidenceFiles = null;
    }
}
