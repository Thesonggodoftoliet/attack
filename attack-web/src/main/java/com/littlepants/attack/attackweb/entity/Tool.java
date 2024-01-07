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
@TableName(value = "tool",autoResultMap = true)
public class Tool {
    @TableId
    private String id;

    @JsonIgnore
    @TableField(value = "create_time")
    private Timestamp createTime;

    @JsonIgnore
    @TableField(value = "update_time")
    private Timestamp updateTime;

    @TableField(value = "tool_name")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z\\d_]{2,60}$",message = "{tool.name.validation}")
    private String toolName;

    @TableField(value = "tool_version")
    private String toolVersion;

    @TableField(value = "tool_description")
    @Size(max = 200,message = "{tool.description.size}")
    private String toolDescription;

    @TableField(value = "applicable_phase_ids",typeHandler = JacksonTypeHandler.class)
    private List<String> phaseIds;

    @TableField(value = "vendor_id")
    private String vendorId;

    @TableField(value = "vendor_name")
    private String vendorName;

    private boolean isBlue;
}
