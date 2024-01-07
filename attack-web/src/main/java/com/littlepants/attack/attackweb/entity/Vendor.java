package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@Data
@TableName(value = "vendor")
public class Vendor {
    @TableId
    private String id;

    @JsonIgnore
    @TableField(value = "create_time")
    private Timestamp createTime;

    @JsonIgnore
    @TableField(value = "update_time")
    private Timestamp updateTime;

    @TableField(value = "vendor_name")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z\\d_]{2,60}$",message = "{vendor.name.validation}")
    private String vendorName;

    @TableField(exist = false)
    private int countOfTools;
}
