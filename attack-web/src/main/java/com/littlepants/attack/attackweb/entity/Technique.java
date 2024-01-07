package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("technique")
public class Technique {
    @TableId
    private String id;
    @TableField("create_time")
    private Timestamp createTime;
    private String label;
    private String category;
}
