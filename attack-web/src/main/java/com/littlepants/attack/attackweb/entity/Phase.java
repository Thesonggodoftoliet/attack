package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "attack_phase")
public class Phase {
    @TableId
    private String id;
    @TableField(value = "create_time")
    private Timestamp createTime;
    private boolean isMetric;
    @TableField(value = "phase_description")
    private String description;
    @TableField(value = "phase_name")
    private String name;
    private String abbreviation;
}
