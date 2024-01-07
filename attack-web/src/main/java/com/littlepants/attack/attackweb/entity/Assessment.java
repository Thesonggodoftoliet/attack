package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Data
@ApiModel
@TableName(value = "assessment",autoResultMap = true)
public class Assessment {
    @TableId
    @ApiModelProperty(value = "攻防报告ID",example = "1111a8b65bd44e72ac631acf68604057")
    private String id;

    @TableField(value = "create_time")
    @ApiModelProperty(hidden = true)
    private Timestamp createTime;

    @TableField(value = "update_time")
    @ApiModelProperty(hidden = true)
    private Timestamp updateTime;

    @TableField(value = "assess_name")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z\\d_][\\s\\u4E00-\\u9FA5A-Za-z\\d_]{1,59}$",message = "{assessment.name.validation}")
    @ApiModelProperty(value = "攻防报告名称",example = "春季检查")
    private String assessName;

    @Size(max = 200,message = "{assessment.description.size}")
    @TableField(value = "assess_description")
    @ApiModelProperty(value = "攻防报告描述",allowEmptyValue = true)
    private String assessDescription;

    @TableField(value = "campaign_ids",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "评估报告中包含的战术ID",allowEmptyValue = true,
            example = "[\"4bdea72467254858b2eb52828e67ad8e\", \"5d6887da10594badbc45d32b1bd34946\"]")
    private List<String> campaignIds;//该评估报告中包含的行动

    @TableField(value = "kill_chain_id")
    @ApiModelProperty(value = "该报告基于的攻击链模型",example = "1")
    private String chainId;//该次报告的攻击链

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private int campaignCounts;
}
