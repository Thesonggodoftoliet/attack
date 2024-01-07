package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@TableName(value = "assets",autoResultMap = true)
public class Assets {
    @TableId
    @ApiModelProperty(value = "资产编号",example = "546ba2f80f9a4577adde21806bc8bd88")
    private String id;

    @JsonIgnore
    @TableField(value = "create_time")
    @ApiModelProperty(hidden = true)
    private Timestamp createTime;

    @JsonIgnore
    @TableField(value = "update_time")
    @ApiModelProperty(hidden = true)
    private Timestamp updateTime;

    @TableField(value = "assets_name")
    @ApiModelProperty(value = "资产名称",example = "MARTINLEE")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z\\d_]{2,20}$",message = "{assets.name.validation}")
    private String assetsName;

    @ApiModelProperty(value = "资产平台",example = "Linux",allowableValues = "Windows,Linux,Other")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z\\d_]{2,20}$",message = "{assets.platform.validation}")
    private String platform;

    @TableField(value = "assets_description")
    @Size(max = 200,message = "{assets.description.size}")
    @ApiModelProperty(value = "资产描述",allowEmptyValue = true)
    private String assetsDescription;

    @TableField(value = "applicable_phase_id",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "适用的攻击阶段ID",example = "[\"01d77cc8-b129-413b-adf5-850cebc4ae79\", \"0d594ca4-4491-4b70-9227-c9e0589dfcca\", \"1b7512d2-ec31-4fab-af57-4fec213b0ae8\"]")
    private List<String> phaseId;//适用的攻击阶段
}
